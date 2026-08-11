package com.pointwest.bootcamp.eventhubri.service;

import com.pointwest.bootcamp.eventhubri.dto.CreateEventRequest;
import com.pointwest.bootcamp.eventhubri.dto.EventResponse;
import com.pointwest.bootcamp.eventhubri.model.Event;
import com.pointwest.bootcamp.eventhubri.repository.EventRepository;
import com.pointwest.bootcamp.eventhubri.service.EventService;

import lombok.RequiredArgsConstructor;
import com.pointwest.bootcamp.eventhubri.dto.AgendaResponse;
import com.pointwest.bootcamp.eventhubri.dto.SessionResponse;
import com.pointwest.bootcamp.eventhubri.model.Agenda;
import com.pointwest.bootcamp.eventhubri.model.BreakSession;
import com.pointwest.bootcamp.eventhubri.model.PresentationSession;
import com.pointwest.bootcamp.eventhubri.model.Session;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    @Override
    public EventResponse createEvent(CreateEventRequest request) {
        Event event = mapToEntity(request);
        // Generate a unique business eventId (e.g., EVT-a1b2c3d4)
        event.setEventId("EVT-" + UUID.randomUUID().toString().substring(0, 8));

        Event savedEvent = eventRepository.save(event);
        return mapToResponse(savedEvent);
    }

    @Override
    public EventResponse getEventByEventId(String eventId) {
        Event event = eventRepository.findByEventId(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found with ID: " + eventId));
        return mapToResponse(event);
    }

    @Override
    public List<EventResponse> getAllEvents() {
        return eventRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public EventResponse updateEvent(String eventId, CreateEventRequest request) {
        Event existingEvent = eventRepository.findByEventId(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found with ID: " + eventId));

        // Update fields from request
        existingEvent.setTitle(request.getTitle());
        existingEvent.setDescription(request.getDescription());
        existingEvent.setOrganizerId(request.getOrganizerId());
        existingEvent.setOrganizerName(request.getOrganizerName());
        existingEvent.setStatus(request.getStatus());
        existingEvent.setStartDateTime(request.getStartDateTime());
        existingEvent.setEndDateTime(request.getEndDateTime());
        existingEvent.setRegistrationOpensAt(request.getRegistrationOpensAt());
        existingEvent.setRegistrationClosesAt(request.getRegistrationClosesAt());
        existingEvent.setVenue(request.getVenue());
        existingEvent.setCapacity(request.getCapacity());

        Event updatedEvent = eventRepository.save(existingEvent);
        return mapToResponse(updatedEvent);
    }

    @Override
    public void deleteEvent(String eventId) {
        Event event = eventRepository.findByEventId(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found with ID: " + eventId));
        eventRepository.delete(event);
    }

    // --- Helper Mapping Methods ---

    private Event mapToEntity(CreateEventRequest request) {
        Event event = new Event();
        event.setTitle(request.getTitle());
        event.setDescription(request.getDescription());
        event.setOrganizerId(request.getOrganizerId());
        event.setOrganizerName(request.getOrganizerName());
        event.setStatus(request.getStatus());
        event.setStartDateTime(request.getStartDateTime());
        event.setEndDateTime(request.getEndDateTime());
        event.setRegistrationOpensAt(request.getRegistrationOpensAt());
        event.setRegistrationClosesAt(request.getRegistrationClosesAt());
        event.setVenue(request.getVenue());
        event.setCapacity(request.getCapacity());
        return event;
    }

    private EventResponse mapToResponse(Event event) {
        EventResponse response = new EventResponse();
        response.setEventId(event.getEventId());
        response.setTitle(event.getTitle());
        response.setDescription(event.getDescription());
        response.setOrganizerId(event.getOrganizerId());
        response.setOrganizerName(event.getOrganizerName());
        response.setStatus(event.getStatus());
        response.setStartDateTime(event.getStartDateTime());
        response.setEndDateTime(event.getEndDateTime());
        response.setRegistrationOpensAt(event.getRegistrationOpensAt());
        response.setRegistrationClosesAt(event.getRegistrationClosesAt());
        response.setVenue(event.getVenue());
        response.setCapacity(event.getCapacity());

        if (event.getAgenda() != null) {
            response.setAgenda(mapToAgendaResponse(event.getAgenda()));
        }

        return response;
    }

    private AgendaResponse mapToAgendaResponse(Agenda agenda) {
        AgendaResponse response = new AgendaResponse();
        response.setDescription(agenda.getDescription());

        if (agenda.getSessions() != null) {
            List<SessionResponse> sessionResponses = agenda.getSessions().stream()
                    .map(this::mapToSessionResponse)
                    .toList();
            response.setSessions(sessionResponses);
        }

        return response;
    }

    private SessionResponse mapToSessionResponse(Session session) {
        SessionResponse response = new SessionResponse();
        response.setAgendaItemId(session.getAgendaItemId());
        response.setTitle(session.getTitle());
        response.setDescription(session.getDescription());
        response.setLocation(session.getLocation());
        response.setStartDateTime(session.getStartDateTime());
        response.setEndDateTime(session.getEndDateTime());

        // Polymorphic handling based on session subtype
        if (session instanceof PresentationSession presentation) {
            response.setSessionType("PRESENTATION");
            response.setSpeaker(presentation.getSpeaker());
        } else if (session instanceof BreakSession breakSession) {
            response.setSessionType("BREAK");
            response.setBreakType(breakSession.getBreakType());
        }

        return response;
    }
}