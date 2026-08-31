package com.pointwest.bootcamp.eventhubri.modules.event.service;

import com.pointwest.bootcamp.eventhubri.modules.event.dto.EventCreateRequestDto;
import com.pointwest.bootcamp.eventhubri.modules.event.dto.EventDiscoveryFilterDto;
import com.pointwest.bootcamp.eventhubri.modules.event.dto.EventDiscoveryResponseDto;
import com.pointwest.bootcamp.eventhubri.modules.event.dto.EventResponseDto;
import com.pointwest.bootcamp.eventhubri.modules.event.dto.EventUpdateRequestDto;
import com.pointwest.bootcamp.eventhubri.modules.event.entity.Event;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EventService {

        EventResponseDto createEvent(
                        EventCreateRequestDto request,
                        String authenticatedUserEmail);

        List<EventResponseDto> getAllEvents(
                        String authenticatedUserEmail);

        EventResponseDto getEventById(
                        Long eventId,
                        String authenticatedUserEmail);

        EventResponseDto updateEvent(
                        Long eventId,
                        EventUpdateRequestDto request,
                        String authenticatedUserEmail);

        void deleteEvent(
                        Long eventId,
                        String authenticatedUserEmail);

        // Attendee Specifics

        Page<EventDiscoveryResponseDto> browsedPublishedEvents(EventDiscoveryFilterDto filter, Pageable pageable);

        EventDiscoveryResponseDto getPublishedEvents(Long eventId);

        // Organizer Specific

        Page<EventResponseDto> getOrganizationEventsPaginated(
                        Event.Status status,
                        Event.EventType eventType,
                        String search,
                        Pageable pageable,
                        String authenticatedUserEmail);
}