package com.pointwest.bootcamp.eventhubri.service;

import com.pointwest.bootcamp.eventhubri.model.*;
import com.pointwest.bootcamp.eventhubri.repository.EventRepository;
import com.pointwest.bootcamp.eventhubri.repository.RegistrationRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class RegistrationService {

    private final EventRepository eventRepository;
    private final RegistrationRepository registrationRepository;

    public RegistrationService(EventRepository eventRepository, RegistrationRepository registrationRepository) {
        this.eventRepository = eventRepository;
        this.registrationRepository = registrationRepository;
    }

    public Registration registerAttendee(String attendeeId, String eventId, String dietaryInfo) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found with ID: " + eventId));

        Registration registration = new Registration();
        registration.setRegistrationId("REG-" + UUID.randomUUID().toString().substring(0, 8));
        registration.setAttendeeId(attendeeId);
        registration.setEventId(event.getEventId());
        registration.setRegisteredAt(new Date());
        registration.setStatus(RegistrationStatus.CONFIRMED);
        registration.setDietaryRestrictions(dietaryInfo);

        return registrationRepository.save(registration);
    }

    public void cancelRegistration(String registrationId) {
        Registration registration = registrationRepository.findById(registrationId)
                .orElseThrow(() -> new IllegalArgumentException("Registration not found with ID: " + registrationId));

        registration.cancel();
        registrationRepository.save(registration);
    }

    public void selectSessions(String registrationId, List<String> sessionIds) {
        Registration registration = registrationRepository.findById(registrationId)
                .orElseThrow(() -> new IllegalArgumentException("Registration not found with ID: " + registrationId));

        Event event = eventRepository.findById(registration.getEventId())
                .orElseThrow(
                        () -> new IllegalArgumentException("Event not found with ID: " + registration.getEventId()));

        if (event.getAgenda() != null && event.getAgenda().getSessions() != null) {
            for (Session session : event.getAgenda().getSessions()) {
                if (sessionIds.contains(session.getSessionId())) {
                    SessionSelection selection = new SessionSelection(
                            "SEL-" + UUID.randomUUID().toString().substring(0, 8),
                            new Date(),
                            session);
                    registration.getSessionSelections().add(selection);
                }
            }
        }

        registrationRepository.save(registration);
    }
}
