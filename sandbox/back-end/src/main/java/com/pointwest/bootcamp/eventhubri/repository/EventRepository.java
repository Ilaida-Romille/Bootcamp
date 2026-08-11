package com.pointwest.bootcamp.eventhubri.repository;

import com.pointwest.bootcamp.eventhubri.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    /**
     * Matches the getEvent(eventId) requirement from your class diagram.
     * Spring Data JPA auto-generates the query behind the scenes based on the method name!
     */
    Optional<Event> findByEventId(String eventId);
}