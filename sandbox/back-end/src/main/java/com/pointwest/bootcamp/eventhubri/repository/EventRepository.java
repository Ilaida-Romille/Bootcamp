package com.pointwest.bootcamp.eventhubri.repository;

import com.pointwest.bootcamp.eventhubri.model.Event;
import java.util.List;
import java.util.Optional;

public interface EventRepository {
    Optional<Event> findById(Long eventId);

    Event save(Event event);
    
    List<Event> findAll();
    List<Event> findByOrganizerId(String organizerId);
    void deleteById(Long eventId);
}
