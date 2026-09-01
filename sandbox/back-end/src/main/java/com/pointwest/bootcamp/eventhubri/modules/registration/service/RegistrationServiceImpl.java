package com.pointwest.bootcamp.eventhubri.modules.registration.service;

import com.pointwest.bootcamp.eventhubri.core.exception.AccessDeniedOperationException;
import com.pointwest.bootcamp.eventhubri.core.exception.BusinessRuleViolationException;
import com.pointwest.bootcamp.eventhubri.core.exception.ResourceNotFoundException;
import com.pointwest.bootcamp.eventhubri.modules.account.entity.AppUser;
import com.pointwest.bootcamp.eventhubri.modules.account.entity.Role;
import com.pointwest.bootcamp.eventhubri.modules.account.repository.AppUserRepository;
import com.pointwest.bootcamp.eventhubri.modules.event.entity.Event;
import com.pointwest.bootcamp.eventhubri.modules.event.repository.EventRepository;
import com.pointwest.bootcamp.eventhubri.modules.registration.dto.RegistrationResponseDto;
import com.pointwest.bootcamp.eventhubri.modules.registration.entity.Registration;
import com.pointwest.bootcamp.eventhubri.modules.registration.entity.RegistrationStatus;
import com.pointwest.bootcamp.eventhubri.modules.registration.repository.RegistrationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {

    private final RegistrationRepository registrationRepository;
    private final EventRepository eventRepository;
    private final AppUserRepository appUserRepository;

    @Override
    @Transactional
    public RegistrationResponseDto register(Long eventId, String attendeeEmail) {
        AppUser attendee = requireUser(attendeeEmail);
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found: " + eventId));

        if (event.getStatus() != Event.Status.PUBLISHED) {
            throw new BusinessRuleViolationException("This event is not open for registration.");
        }
        if (event.getStartTime() != null && event.getStartTime().isBefore(LocalDateTime.now())) {
            throw new BusinessRuleViolationException("This event has already started; registration is closed.");
        }
        if (registrationRepository.existsByEvent_IdAndAttendee_Id(eventId, attendee.getId())) {
            throw new BusinessRuleViolationException("You are already registered for this event.");
        }

        RegistrationStatus status = RegistrationStatus.CONFIRMED;
        if (event.getMaxCapacity() != null) {
            long confirmedCount = registrationRepository.countByEvent_IdAndStatus(eventId,
                    RegistrationStatus.CONFIRMED);
            if (confirmedCount >= event.getMaxCapacity()) {
                status = RegistrationStatus.WAITLISTED;
            }
        }

        Registration registration = Registration.builder()
                .event(event)
                .attendee(attendee)
                .status(status)
                .build();

        return toResponseDto(registrationRepository.save(registration));
    }

    @Override
    public RegistrationResponseDto getOwnRegistration(Long registrationId, String attendeeEmail) {
        AppUser attendee = requireUser(attendeeEmail);
        return toResponseDto(findOwned(registrationId, attendee.getId()));
    }

    @Override
    public Page<RegistrationResponseDto> listOwnRegistrations(String attendeeEmail, Pageable pageable) {
        AppUser attendee = requireUser(attendeeEmail);
        return registrationRepository.findByAttendee_Id(attendee.getId(), pageable).map(this::toResponseDto);
    }

    @Override
    @Transactional
    public void cancelOwnRegistration(Long registrationId, String attendeeEmail) {
        AppUser attendee = requireUser(attendeeEmail);
        Registration registration = findOwned(registrationId, attendee.getId());

        if (registration.getStatus() == RegistrationStatus.CANCELLED) {
            throw new BusinessRuleViolationException("This registration is already cancelled.");
        }

        cancelRegistration(registration);
    }

    @Override
    @Transactional
    public void removeRegistrationForOrganizer(Long registrationId, String staffEmail) {
        AppUser staff = requireUser(staffEmail);
        Registration registration = registrationRepository.findById(registrationId)
                .orElseThrow(() -> new ResourceNotFoundException("Registration not found: " + registrationId));

        assertSameOrganization(staff, registration.getEvent());
        if (registration.getEvent().getRegistrationEndTime() != null
                && LocalDateTime.now().isAfter(registration.getEvent().getRegistrationEndTime())) {
            throw new BusinessRuleViolationException("The registration period for this event has ended.");
        }

        cancelRegistration(registration);
    }

    @Override
    public Page<RegistrationResponseDto> listRegistrationsForEvent(
            Long eventId, RegistrationStatus statusFilter, String staffEmail, Pageable pageable) {
        Event event = requireManagedEvent(eventId, staffEmail);
        Page<Registration> page = (statusFilter != null)
                ? registrationRepository.findByEvent_IdAndStatus(event.getId(), statusFilter, pageable)
                : registrationRepository.findByEvent_Id(event.getId(), pageable);
        return page.map(this::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RegistrationResponseDto> listConfirmedAttendeesForEvent(Long eventId, Pageable pageable) {
        return registrationRepository
                .findByEvent_IdAndStatus(eventId, RegistrationStatus.CONFIRMED, pageable)
                .map(this::toResponseDto);
    }

    @Override
    @Transactional
    public RegistrationResponseDto checkInAttendee(Long registrationId, String staffEmail) {
        AppUser staff = requireUser(staffEmail);
        Registration registration = registrationRepository.findById(registrationId)
                .orElseThrow(() -> new ResourceNotFoundException("Registration not found: " + registrationId));

        assertSameOrganization(staff, registration.getEvent());

        if (registration.getStatus() != RegistrationStatus.CONFIRMED) {
            throw new BusinessRuleViolationException("Only confirmed registrations can be checked in.");
        }
        registration.setCheckedInAt(LocalDateTime.now());
        return toResponseDto(registrationRepository.save(registration));
    }

    @Override
    @Transactional
    public RegistrationResponseDto updateStatus(Long registrationId, RegistrationStatus newStatus, String staffEmail) {
        AppUser staff = requireUser(staffEmail);
        Registration registration = registrationRepository.findById(registrationId)
                .orElseThrow(() -> new ResourceNotFoundException("Registration not found: " + registrationId));

        assertSameOrganization(staff, registration.getEvent());
        registration.setStatus(newStatus);
        return toResponseDto(registrationRepository.save(registration));
    }

    private void cancelRegistration(Registration registration) {
        if (registration.getStatus() == RegistrationStatus.CANCELLED) {
            throw new BusinessRuleViolationException("This registration is already cancelled.");
        }

        boolean wasConfirmed = registration.getStatus() == RegistrationStatus.CONFIRMED;
        registration.setStatus(RegistrationStatus.CANCELLED);
        registrationRepository.save(registration);

        if (wasConfirmed) {
            registrationRepository
                    .findFirstByEvent_IdAndStatusOrderByCreatedAtAsc(
                            registration.getEvent().getId(), RegistrationStatus.WAITLISTED)
                    .ifPresent(next -> {
                        next.setStatus(RegistrationStatus.CONFIRMED);
                        registrationRepository.save(next);
                    });
        }
    }

    private Registration findOwned(Long registrationId, Long attendeeId) {
        return registrationRepository.findByIdAndAttendee_Id(registrationId, attendeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Registration not found: " + registrationId));
    }

    private Event requireManagedEvent(Long eventId, String staffEmail) {
        AppUser staff = requireUser(staffEmail);
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found: " + eventId));
        assertSameOrganization(staff, event);
        return event;
    }

    private AppUser requireUser(String email) {
        return appUserRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found."));
    }

    private void assertSameOrganization(AppUser staff, Event event) {
        boolean isPlatformAdmin = staff.getRole() == Role.PLATFORM_ADMIN;
        boolean sameOrg = staff.getOrganization() != null
                && event.getOrganization() != null
                && staff.getOrganization().getId().equals(event.getOrganization().getId());
        if (!isPlatformAdmin && !sameOrg) {
            throw new AccessDeniedOperationException("You do not have access to this event's registrations.");
        }
    }

    private RegistrationResponseDto toResponseDto(Registration registration) {
        Event event = registration.getEvent();
        AppUser attendee = registration.getAttendee();
        return new RegistrationResponseDto(
                registration.getId(),
                event.getId(),
                event.getTitle(),
                event.getStartTime(),
                event.getEndTime(),
                attendee.getId(),
                attendee.getFirstName() + " " + attendee.getLastName(),
                registration.getStatus(),
                registration.getCheckedInAt(),
                registration.getCreatedAt());
    }
}
