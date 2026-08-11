package com.pointwest.bootcamp.eventhubri.repository;

import com.pointwest.bootcamp.eventhubri.model.Event;
import com.pointwest.bootcamp.eventhubri.model.EventStatus;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class EventRepository {
    private final List<Event> events = new ArrayList<>();

    public EventRepository() {
        // Pre-seeded mock event
        Event event = new Event();
        event.setEventId("E1001");
        event.setTitle("Tech Summit 2026");
        event.setDescription("Annual Tech Conference");
        event.setStatus(EventStatus.PUBLISHED);
        event.setCapacity(100);
        event.setFoodProvided(true);
        event.setOrganizerId("O201");
        event.setStartDateTime(LocalDateTime.now().plusDays(10));
        event.setEndDateTime(LocalDateTime.now().plusDays(10).plusHours(4));
        this.events.add(event);
    }

    public List<Event> findAll() {
        return new ArrayList<>(this.events);
    }

    public Optional<Event> findById(String eventId) {
        return this.events.stream()
                .filter(event -> event.getEventId().equals(eventId))
                .findFirst();
    }

    public List<Event> findByOrganizerId(String organizerId) {
        return this.events.stream()
                .filter(event -> event.getOrganizerId().equals(organizerId))
                .collect(Collectors.toList());
    }

    public Event save(Event event) {
        this.events.removeIf(e -> e.getEventId().equals(event.getEventId()));
        this.events.add(event);
        return event;
    }
}