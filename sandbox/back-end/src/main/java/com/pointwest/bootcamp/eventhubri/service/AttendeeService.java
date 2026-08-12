package com.pointwest.bootcamp.eventhubri.service;

import com.pointwest.bootcamp.eventhubri.model.Agenda;
import com.pointwest.bootcamp.eventhubri.model.Event;
import com.pointwest.bootcamp.eventhubri.model.Registration;
import com.pointwest.bootcamp.eventhubri.model.Session;
import com.pointwest.bootcamp.eventhubri.repository.EventRepository;
import com.pointwest.bootcamp.eventhubri.repository.RegistrationRepository;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AttendeeService {

    private final EventRepository eventRepository;
    private final RegistrationRepository registrationRepository;

    public AttendeeService(EventRepository eventRepository, RegistrationRepository registrationRepository) {
        this.eventRepository = eventRepository;
        this.registrationRepository = registrationRepository;
    }

    // --- Read-Only Event Views ---

    public List<Event> getAllAvailableEvents() {
        return eventRepository.findAll();
    }

    public Event getEventDetails(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found with ID: " + eventId));
    }

    // --- Agenda & Session Views ---

    public Agenda getEventAgenda(Long eventId) {
        Event event = getEventDetails(eventId);
        return event.getAgenda();
    }

    public List<Session> getEventSessions(Long eventId) {
        Event event = getEventDetails(eventId);
        if (event.getAgenda() == null || event.getAgenda().getSessions() == null) {
            return Collections.emptyList();
        }
        return event.getAgenda().getSessions();
    }

    // --- Attendee Context Views ---

    public List<Registration> getMyRegistrations(String attendeeId) {
        return registrationRepository.findByAttendeeId(attendeeId);
    }
}