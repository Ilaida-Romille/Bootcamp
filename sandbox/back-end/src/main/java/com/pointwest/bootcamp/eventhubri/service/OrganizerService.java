package com.pointwest.bootcamp.eventhubri.service;

import com.pointwest.bootcamp.eventhubri.model.*;
import com.pointwest.bootcamp.eventhubri.repository.EventRepository;
import com.pointwest.bootcamp.eventhubri.repository.RegistrationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class OrganizerService {

    private final EventRepository eventRepository;
    private final RegistrationRepository registrationRepository;

    public OrganizerService(EventRepository eventRepository, RegistrationRepository registrationRepository) {
        this.eventRepository = eventRepository;
        this.registrationRepository = registrationRepository;
    }

    // --- Event CRUD Operations ---

    public Event createEvent(Event event) {
        // if (event.getEventId() == null || event.getEventId().isBlank()) {
        //     event.setEventId("EVT-" + UUID.randomUUID().toString().substring(0, 8));
        // }
        return eventRepository.save(event);
    }

    public Event updateEvent(Long eventId, Event updatedEventDetails) {
        Event existingEvent = getEventById(eventId);

        existingEvent.setTitle(updatedEventDetails.getTitle());
        existingEvent.setDescription(updatedEventDetails.getDescription());
        existingEvent.setStatus(updatedEventDetails.getStatus());
        existingEvent.setStartDateTime(updatedEventDetails.getStartDateTime());
        existingEvent.setEndDateTime(updatedEventDetails.getEndDateTime());
        existingEvent.setRegistrationOpensAt(updatedEventDetails.getRegistrationOpensAt());
        existingEvent.setRegistrationClosesAt(updatedEventDetails.getRegistrationClosesAt());
        existingEvent.setVenue(updatedEventDetails.getVenue());
        existingEvent.setCapacity(updatedEventDetails.getCapacity());
        existingEvent.setIsFoodProvided(updatedEventDetails.getIsFoodProvided());

        return eventRepository.save(existingEvent);
    }

    public void deleteEvent(Long eventId) {
        getEventById(eventId); // Throws exception if not found
        eventRepository.deleteById(eventId);
    }

    public Event getEventById(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found with ID: " + eventId));
    }

    public List<Event> getEventsByOrganizer(String organizerId) {
        return eventRepository.findByOrganizerId(organizerId);
    }

    // --- Agenda & Session Management ---

    public Agenda attachAgendaToEvent(Long eventId, Agenda agenda) {
        Event event = getEventById(eventId);
        if (agenda.getAgendaId() == null) {
            agenda.setAgendaId("AGN-" + UUID.randomUUID().toString().substring(0, 8));
        }
        event.setAgenda(agenda);
        eventRepository.save(event);
        return agenda;
    }

    public void addSessionToEventAgenda(Long eventId, Session session) {
        Event event = getEventById(eventId);
        if (event.getAgenda() == null) {
            Agenda newAgenda = new Agenda();
            newAgenda.setAgendaId("AGN-" + UUID.randomUUID().toString().substring(0, 8));
            newAgenda.setSessions(new ArrayList<>());
            event.setAgenda(newAgenda);
        }

        if (session.getSessionId() == null) {
            session.setSessionId("SES-" + UUID.randomUUID().toString().substring(0, 8));
        }

        event.getAgenda().getSessions().add(session);
        eventRepository.save(event);
    }

    // --- Organizer Insights ---

    public List<Registration> getEventRegistrations(Long eventId) {
        getEventById(eventId); // Verify event existence
        return registrationRepository.findByEventId(eventId);
    }
}