package com.pointwest.bootcamp.eventhubri.modules.event.service;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.pointwest.bootcamp.eventhubri.core.exception.ResourceNotFoundException;
import com.pointwest.bootcamp.eventhubri.modules.account.repository.AppUserRepository;
import com.pointwest.bootcamp.eventhubri.modules.event.dto.EventDiscoveryFilterDto;
import com.pointwest.bootcamp.eventhubri.modules.event.dto.EventDiscoveryResponseDto;
import com.pointwest.bootcamp.eventhubri.modules.event.entity.Event;
import com.pointwest.bootcamp.eventhubri.modules.event.repository.EventRepository;
import com.pointwest.bootcamp.eventhubri.modules.registration.entity.RegistrationStatus;
import com.pointwest.bootcamp.eventhubri.modules.registration.repository.RegistrationRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class EventAttendeeService extends EventServiceImpl {

    private final EventRepository eventRepository;
    private final RegistrationRepository registrationRepository;

    public EventAttendeeService(AppUserRepository appUserRepository, EventRepository eventRepository,
            RegistrationRepository registrationRepository) {
        super(eventRepository, appUserRepository);
        this.eventRepository = eventRepository;
        this.registrationRepository = registrationRepository;
    }

    @Override
    public Page<EventDiscoveryResponseDto> browsedPublishedEvents(EventDiscoveryFilterDto filter, Pageable pageable) {
        return eventRepository.searchDiscoverable(
                filter.keyword(), filter.eventType(), filter.startFrom(), filter.startTo(), filter.location(), pageable)
                .map(this::toDiscoveryDto);
    }

    @Override
    public EventDiscoveryResponseDto getPublishedEvents(Long eventId) {
        Event event = eventRepository.findByIdAndStatusAndIsPrivateFalse(eventId, Event.Status.PUBLISHED)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Event not found or not open to the public: " + eventId));
        return toDiscoveryDto(event);
    }

    private EventDiscoveryResponseDto toDiscoveryDto(Event event) {
        Integer availableSlots = null;
        boolean registrationOpen = event.getStartTime() == null || event.getStartTime().isAfter(LocalDateTime.now());
        if (event.getMaxCapacity() != null) {
            long confirmed = registrationRepository.countByEvent_IdAndStatus(event.getId(),
                    RegistrationStatus.CONFIRMED);
            availableSlots = (int) Math.max(0, event.getMaxCapacity() - confirmed);
            registrationOpen = registrationOpen && availableSlots > 0;
        }
        return new EventDiscoveryResponseDto(
                event.getId(),
                event.getOrganization().getId(),
                event.getOrganization().getCompanyName(),
                event.getTitle(),
                event.getDescription(),
                event.getBannerImageUrl(),
                event.getEventType(),
                event.getLocationAddress(),
                event.getVirtualMeetingUrl(),
                event.getStartTime(),
                event.getEndTime(),
                event.isCateringProvided(),
                event.getMaxCapacity(),
                availableSlots,
                registrationOpen);
    }

}
