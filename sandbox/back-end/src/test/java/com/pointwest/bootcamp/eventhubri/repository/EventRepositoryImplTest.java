package com.pointwest.bootcamp.eventhubri.repository;

import com.pointwest.bootcamp.eventhubri.model.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class EventRepositoryImplTest {

    private EventRepositoryImpl eventRepository;

    @BeforeEach
    public void setUp() {
        eventRepository = new EventRepositoryImpl();
    }

    @Test
    public void testInitialMockDataSeeded() {
        List<Event> allEvents = eventRepository.findAll();
        assertFalse(allEvents.isEmpty());

        Optional<Event> seededEvent = eventRepository.findById(101L);
        assertTrue(seededEvent.isPresent());
        assertEquals("Tech Summit 2026", seededEvent.get().getTitle());
    }

    @Test
    public void testSaveAndFindById() {
        Event event = new Event();
        event.setEventId(999L);
        event.setTitle("Spring Boot Workshop");

        eventRepository.save(event);

        Optional<Event> found = eventRepository.findById(999L);
        assertTrue(found.isPresent());
        assertEquals("Spring Boot Workshop", found.get().getTitle());
    }

    @Test
    public void testSave_UpdatesExistingEvent() {
        Event event = new Event();
        event.setEventId(101L); // Existing seeded ID
        event.setTitle("Updated Tech Summit 2026");

        eventRepository.save(event);

        Optional<Event> found = eventRepository.findById(101L);
        assertTrue(found.isPresent());
        assertEquals("Updated Tech Summit 2026", found.get().getTitle());
    }

    @Test
    public void testFindByOrganizerId() {
        Event event = new Event();
        event.setEventId(200L);
        event.setOrganizerId("ORG-007");

        eventRepository.save(event);

        List<Event> results = eventRepository.findByOrganizerId("ORG-007");
        assertEquals(1, results.size());
        assertEquals(200L, results.get(0).getEventId());
    }

    @Test
    public void testDeleteById() {
        Event event = new Event();
        event.setEventId(300L);

        eventRepository.save(event);
        assertTrue(eventRepository.findById(300L).isPresent());

        eventRepository.deleteById(300L);
        assertTrue(eventRepository.findById(300L).isEmpty());
    }

    @Test
    public void testNullInputs_ReturnEmptyResultsSafely() {
        assertTrue(eventRepository.findById(null).isEmpty());
        assertTrue(eventRepository.findByOrganizerId(null).isEmpty());
    }
}