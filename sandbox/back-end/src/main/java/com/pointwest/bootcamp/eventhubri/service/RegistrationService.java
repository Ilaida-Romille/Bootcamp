package com.pointwest.bootcamp.eventhubri.service;

import com.pointwest.bootcamp.eventhubri.dto.SessionAttendeeCountDto;
import com.pointwest.bootcamp.eventhubri.dto.SessionPopularitySummaryDto;
import com.pointwest.bootcamp.eventhubri.model.*;
import com.pointwest.bootcamp.eventhubri.repository.EventRepository;
import com.pointwest.bootcamp.eventhubri.repository.RegistrationRepository;

import com.pointwest.bootcamp.eventhubri.dto.SessionPopularityStatsProjectionDto;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Date;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.UUID;

@Service
@Transactional
public class RegistrationService {

    private final EventRepository eventRepository;
    private final RegistrationRepository registrationRepository;
    //add repo for session speicfically
    public RegistrationService(EventRepository eventRepository, RegistrationRepository registrationRepository) {
        this.eventRepository = eventRepository;
        this.registrationRepository = registrationRepository;
    }

    public Registration registerAttendee(String attendeeId, Long eventId, String dietaryInfo) {
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

    public SessionPopularitySummaryDto getSessionPopularitySummary(Long eventId) {
        
        List<SessionAttendeeCountDto> sessionCounts = 
                registrationRepository.countAttendeesPerSessionByEventId(eventId);

        SessionPopularityStatsProjectionDto stats = 
                registrationRepository.getSessionPopularityStatsByEventId(eventId);

        long count = (stats != null && stats.getCount() != null) ? stats.getCount() : 0L;
        long sum = (stats != null && stats.getSum() != null) ? stats.getSum() : 0L;
        double average = (stats != null && stats.getAverage() != null) ? stats.getAverage() : 0.0;
        long min = (stats != null && stats.getMin() != null) ? stats.getMin() : 0L;
        long max = (stats != null && stats.getMax() != null) ? stats.getMax() : 0L;

        return new SessionPopularitySummaryDto(
                eventId,
                sessionCounts,
                count,
                sum,
                average,
                min,
                max
        );
    }

    public List<Session> searchSessionsByTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            return List.of();
        }
        return eventRepository.findSessionsByTitle(title.trim());
    }
}
