package com.pointwest.bootcamp.eventhubri.config;

import com.pointwest.bootcamp.eventhubri.model.Event;
import com.pointwest.bootcamp.eventhubri.model.EventStatus;
import com.pointwest.bootcamp.eventhubri.repository.EventRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class DataInitializer implements CommandLineRunner {

    private final EventRepository eventRepository;

    public DataInitializer(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public void run(String... args) {
        // Seed default event 101 if it doesn't already exist
        if (eventRepository.findById(101L).isEmpty()) {
            Event sampleEvent = new Event();
            sampleEvent.setTitle("Tech Summit 2026");
            sampleEvent.setDescription("Annual developer conference");
            sampleEvent.setStatus(EventStatus.PUBLISHED);
            sampleEvent.setStartDateTime(new Date());
            sampleEvent.setEndDateTime(new Date(System.currentTimeMillis() + 86400000L));
            sampleEvent.setVenue("Main Hall");
            sampleEvent.setCapacity(500);
            sampleEvent.setIsFoodProvided(true);

            eventRepository.save(sampleEvent);
        }
    }
}