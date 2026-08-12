package com.pointwest.bootcamp.eventhubri.repository;

import com.pointwest.bootcamp.eventhubri.model.Event;
import java.util.Optional;

public interface EventRepository {
    Optional<Event> findById(String eventId);

    Event save(Event event);
}
