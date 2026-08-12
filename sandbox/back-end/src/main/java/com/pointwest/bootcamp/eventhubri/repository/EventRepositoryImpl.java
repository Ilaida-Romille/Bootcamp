package com.pointwest.bootcamp.eventhubri.repository;

import com.pointwest.bootcamp.eventhubri.model.Event;
import com.pointwest.bootcamp.eventhubri.model.EventStatus;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public class EventRepositoryImpl implements EventRepository {
    private final List<Event> events = new ArrayList<>();

    public EventRepositoryImpl() {
        // Seed mock data
        Event sampleEvent = new Event();
        sampleEvent.setEventId("EVT-101");
        sampleEvent.setTitle("Tech Summit 2026");
        sampleEvent.setDescription("Annual developer conference");
        sampleEvent.setStatus(EventStatus.PUBLISHED);
        sampleEvent.setStartDateTime(new Date());
        sampleEvent.setEndDateTime(new Date(System.currentTimeMillis() + 86400000L));
        sampleEvent.setVenue("Main Hall");
        sampleEvent.setCapacity(500);
        sampleEvent.setIsFoodProvided(true);
        events.add(sampleEvent);
    }

    @Override
    public Optional<Event> findById(String eventId) {
        if (eventId == null)
            return Optional.empty();
        return events.stream()
                .filter(e -> eventId.equals(e.getEventId()))
                .findFirst();
    }

    @Override
    public Event save(Event event) {
        events.removeIf(e -> e.getEventId().equals(event.getEventId()));
        events.add(event);
        return event;
    }
}
