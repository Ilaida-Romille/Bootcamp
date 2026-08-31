package com.pointwest.bootcamp.eventhubri.modules.event.service;

import com.pointwest.bootcamp.eventhubri.modules.account.entity.AppUser;
import com.pointwest.bootcamp.eventhubri.modules.account.repository.AppUserRepository;
import com.pointwest.bootcamp.eventhubri.modules.event.dto.EventCreateRequestDto;
import com.pointwest.bootcamp.eventhubri.modules.event.dto.EventResponseDto;
import com.pointwest.bootcamp.eventhubri.modules.event.dto.EventUpdateRequestDto;
import com.pointwest.bootcamp.eventhubri.modules.event.entity.Event;
import com.pointwest.bootcamp.eventhubri.modules.event.entity.Event.EventType;
import com.pointwest.bootcamp.eventhubri.modules.event.entity.Event.Status;
import com.pointwest.bootcamp.eventhubri.modules.event.exception.EventNotFoundException;
import com.pointwest.bootcamp.eventhubri.modules.event.repository.EventRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import javax.management.RuntimeErrorException;

@Service
@RequiredArgsConstructor
@Transactional
public class EventOrganizerServiceImpl implements EventOrganizerService {

    private final EventRepository eventRepository;
    private final AppUserRepository appUserRepository;

    @Override
    public EventResponseDto createEvent(
            EventCreateRequestDto request,
            String authenticatedUserEmail) {

        AppUser user = getAuthenticatedUser(authenticatedUserEmail);

        if (user.getOrganization() == null) {
            throw new IllegalStateException(
                    "User is not associated with an organization");
        }

        validateEventTimes(
                request.startTime(),
                request.endTime());

        validateEventTypeRequirements(
                request.eventType(),
                request.locationAddress(),
                request.virtualMeetingUrl());

        Event event = Event.builder()
                .organization(user.getOrganization())
                .createdBy(user)
                .title(request.title())
                .description(request.description())
                .bannerImageUrl(request.bannerImageUrl())
                .eventType(request.eventType())
                .locationAddress(request.locationAddress())
                .virtualMeetingUrl(request.virtualMeetingUrl())
                .startTime(request.startTime())
                .endTime(request.endTime())
                .registrationStartTime(request.registrationOpensAt())
                .registrationEndTime(request.registrationClosesAt())
                .isPrivate(request.isPrivate())
                .cateringProvided(request.cateringProvided())
                .maxCapacity(request.maxCapacity())
                .status(Event.Status.DRAFT)
                .build();

        Event savedEvent = eventRepository.save(event);

        return toResponse(savedEvent);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventResponseDto> getAllEvents(
            String authenticatedUserEmail) {

        AppUser user = getAuthenticatedUser(authenticatedUserEmail);

        if (user.getOrganization() == null) {
            throw new IllegalStateException(
                    "User is not associated with an organization");
        }

        return eventRepository
                .findByOrganizationId(user.getOrganization().getId())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public EventResponseDto getEventById(
            Long eventId,
            String authenticatedUserEmail) {

        AppUser user = getAuthenticatedUser(authenticatedUserEmail);

        Long organizationId = getOrganizationId(user);

        Event event = eventRepository
                .findByIdAndOrganizationId(eventId, organizationId)
                .orElseThrow(() -> new EventNotFoundException(eventId));

        return toResponse(event);
    }

    @Override
    public EventResponseDto updateEvent(
            Long eventId,
            EventUpdateRequestDto request,
            String authenticatedUserEmail) {

        AppUser user = getAuthenticatedUser(authenticatedUserEmail);

        Long organizationId = getOrganizationId(user);

        Event event = eventRepository
                .findByIdAndOrganizationId(eventId, organizationId)
                .orElseThrow(() -> new EventNotFoundException(eventId));

        updateFields(event, request);

        validateEventTimes(
                event.getStartTime(),
                event.getEndTime());

        validateEventTypeRequirements(
                event.getEventType(),
                event.getLocationAddress(),
                event.getVirtualMeetingUrl());

        return toResponse(eventRepository.save(event));
    }

    @Override
    public void deleteEvent(
            Long eventId,
            String authenticatedUserEmail) {

        AppUser user = getAuthenticatedUser(authenticatedUserEmail);

        Long organizationId = getOrganizationId(user);

        Event event = eventRepository
                .findByIdAndOrganizationId(eventId, organizationId)
                .orElseThrow(() -> new EventNotFoundException(eventId));

        eventRepository.delete(event);
    }

    @Override
    public Page<EventResponseDto> getOrganizationEventsPaginated(Status status, EventType eventType, String search,
            Pageable pageable, String authenticatedUserEmail) {
        AppUser user = getAuthenticatedUser(authenticatedUserEmail);
        Long organizationId = getOrganizationId(user);

        String cleanKeyword = (search != null && !search.isBlank()) ? search.trim() : null;

        return eventRepository
                .searchOrganizationEvents(organizationId, status, eventType, cleanKeyword, pageable)
                .map(this::toResponse);
    }

    private AppUser getAuthenticatedUser(String email) {

        return appUserRepository
                .findByEmail(email)
                .orElseThrow(() -> new RuntimeException(
                        "Authenticated user not found"));
    }

    private Long getOrganizationId(AppUser user) {

        if (user.getOrganization() == null) {
            throw new IllegalStateException(
                    "User is not associated with an organization");
        }

        return user.getOrganization().getId();
    }

    private void updateFields(
            Event event,
            EventUpdateRequestDto request) {

        if (request.title() != null) {
            event.setTitle(request.title());
        }

        if (request.description() != null) {
            event.setDescription(request.description());
        }

        if (request.bannerImageUrl() != null) {
            event.setBannerImageUrl(request.bannerImageUrl());
        }

        if (request.eventType() != null) {
            event.setEventType(request.eventType());
        }

        if (request.locationAddress() != null) {
            event.setLocationAddress(request.locationAddress());
        }

        if (request.virtualMeetingUrl() != null) {
            event.setVirtualMeetingUrl(request.virtualMeetingUrl());
        }

        if (request.startTime() != null) {
            event.setStartTime(request.startTime());
        }

        if (request.endTime() != null) {
            event.setEndTime(request.endTime());
        }

        if (request.isPrivate() != null) {
            event.setPrivate(request.isPrivate());
        }

        if (request.cateringProvided() != null) {
            event.setCateringProvided(request.cateringProvided());
        }

        if (request.maxCapacity() != null) {
            event.setMaxCapacity(request.maxCapacity());
        }

        if (request.status() != null) {
            event.setStatus(request.status());
        }
    }

    // private AppUser getAuthenticatedUserById(String identifier){
    // Long userId = Long.parseLong(identifier);
    // return appUserRepository.findByIdOptional(userId).orElseThrow(() -> new
    // RuntimeException("Cannot find user"));
    // }

    private void validateEventTimes(
            java.time.LocalDateTime startTime,
            java.time.LocalDateTime endTime) {

        if (startTime == null || endTime == null) {
            return;
        }

        if (!endTime.isAfter(startTime)) {
            throw new IllegalArgumentException(
                    "Event end time must be after start time");
        }
    }

    private void validateEventTypeRequirements(
            Event.EventType eventType,
            String locationAddress,
            String virtualMeetingUrl) {

        if (eventType == null) {
            return;
        }

        switch (eventType) {

            case PHYSICAL -> {
                if (locationAddress == null ||
                        locationAddress.isBlank()) {

                    throw new IllegalArgumentException(
                            "Location address is required for physical events");
                }
            }

            case VIRTUAL -> {
                if (virtualMeetingUrl == null ||
                        virtualMeetingUrl.isBlank()) {

                    throw new IllegalArgumentException(
                            "Virtual meeting URL is required for virtual events");
                }
            }

            case HYBRID -> {

                if (locationAddress == null ||
                        locationAddress.isBlank()) {

                    throw new IllegalArgumentException(
                            "Location address is required for hybrid events");
                }

                if (virtualMeetingUrl == null ||
                        virtualMeetingUrl.isBlank()) {

                    throw new IllegalArgumentException(
                            "Virtual meeting URL is required for hybrid events");
                }
            }
        }
    }

    private EventResponseDto toResponse(Event event) {

        String createdByName = event.getCreatedBy().getFirstName()
                + " "
                + event.getCreatedBy().getLastName();

        return new EventResponseDto(
                event.getId(),
                event.getOrganization().getId(),
                event.getOrganization().getCompanyName(),
                event.getCreatedBy().getId(),
                createdByName,
                event.getTitle(),
                event.getDescription(),
                event.getBannerImageUrl(),
                event.getEventType(),
                event.getLocationAddress(),
                event.getVirtualMeetingUrl(),
                event.getStartTime(),
                event.getEndTime(),
                event.getRegistrationStartTime(),
                event.getRegistrationEndTime(),
                event.isPrivate(),
                event.isCateringProvided(),
                event.getMaxCapacity(),
                event.getStatus(),
                event.getCreatedAt(),
                event.getUpdatedAt());
    }
}