package com.pointwest.bootcamp.eventhubri.repository;

import com.pointwest.bootcamp.eventhubri.model.Registration;
import com.pointwest.bootcamp.eventhubri.model.RegistrationStatus;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class RegistrationRepository {
    private final List<Registration> registrations = new ArrayList<>();

    public List<Registration> findAll() {
        return new ArrayList<>(this.registrations);
    }

    public Optional<Registration> findById(String registrationId) {
        return this.registrations.stream()
                .filter(reg -> reg.getRegistrationId().equals(registrationId))
                .findFirst();
    }

    public List<Registration> findByEventId(String eventId) {
        return this.registrations.stream()
                .filter(reg -> reg.getEvent() != null && reg.getEvent().getEventId().equals(eventId))
                .collect(Collectors.toList());
    }

    public List<Registration> findByAttendeeId(String attendeeId) {
        return this.registrations.stream()
                .filter(reg -> reg.getAttendee() != null && reg.getAttendee().getAttendeeId().equals(attendeeId))
                .collect(Collectors.toList());
    }

    public boolean existsByAttendeeIdAndEventId(String attendeeId, String eventId) {
        return this.registrations.stream()
                .anyMatch(reg -> reg.getAttendee() != null 
                        && reg.getAttendee().getAttendeeId().equals(attendeeId)
                        && reg.getEvent() != null 
                        && reg.getEvent().getEventId().equals(eventId)
                        && reg.getStatus() != RegistrationStatus.CANCELLED);
    }

    public long countByEventIdAndStatus(String eventId, RegistrationStatus status) {
        return this.registrations.stream()
                .filter(reg -> reg.getEvent() != null 
                        && reg.getEvent().getEventId().equals(eventId)
                        && reg.getStatus() == status)
                .count();
    }

    public Registration save(Registration registration) {
        this.registrations.removeIf(reg -> reg.getRegistrationId().equals(registration.getRegistrationId()));
        this.registrations.add(registration);
        return registration;
    }
}