package com.pointwest.bootcamp.eventhubri.modules.event.service;

import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.pointwest.bootcamp.eventhubri.core.exception.ResourceNotFoundException;
import com.pointwest.bootcamp.eventhubri.modules.event.dto.EventDiscoveryFilterDto;
import com.pointwest.bootcamp.eventhubri.modules.event.dto.EventDiscoveryResponseDto;
import com.pointwest.bootcamp.eventhubri.modules.event.entity.Event;
import com.pointwest.bootcamp.eventhubri.modules.event.repository.EventRepository;
import com.pointwest.bootcamp.eventhubri.modules.registration.entity.RegistrationStatus;
import com.pointwest.bootcamp.eventhubri.modules.registration.repository.RegistrationRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class EventAttendeeServiceImpl implements EventAttendeeService {

    private final EventRepository eventRepository;
    private final RegistrationRepository registrationRepository;
    @Override
    public Page<EventDiscoveryResponseDto> browsedPublishedEvents(EventDiscoveryFilterDto filter, Long userOrgId,
            Long userId, Pageable pageable) {
        Page<Event> events = eventRepository.searchDiscoverable(
                userOrgId,
                filter.keyword(),
                filter.eventType(),
                filter.startFrom(),
                filter.startTo(),
                filter.location(),
                pageable);
        Set<Long> registeredIds = registrationRepository.findActiveRegisteredEventIds(userId);
        return events.map(event -> toDiscoveryDto(event, registeredIds.contains(event.getId())));
    }

    @Override
    public EventDiscoveryResponseDto getPublishedEvents(Long eventId, Long organizerId, Long userId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));

        if (event.isPrivate() && !event.getOrganization().getId().equals(organizerId)) {
            throw new AccessDeniedException("You do not have permission to view this event.");
        }

        boolean isRegistered = registrationRepository
                .existsByEvent_IdAndAttendee_Id(eventId, userId);
        return toDiscoveryDto(event, isRegistered);
    }

    private EventDiscoveryResponseDto toDiscoveryDto(Event event, boolean isRegistered) {
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
                registrationOpen,
                isRegistered);
    }

}
