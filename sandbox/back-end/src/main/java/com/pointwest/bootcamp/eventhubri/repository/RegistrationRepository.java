package com.pointwest.bootcamp.eventhubri.repository;

import com.pointwest.bootcamp.eventhubri.model.Registration;
import java.util.List;
import java.util.Optional;

public interface RegistrationRepository {
    List<Registration> findByEventId(String eventId);

    List<Registration> findByAttendeeId(String attendeeId);

    Optional<Registration> findById(String registrationId);

    Registration save(Registration registration);
}
