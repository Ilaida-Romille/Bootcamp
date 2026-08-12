package com.pointwest.bootcamp.eventhubri.repository;

import com.pointwest.bootcamp.eventhubri.model.Event;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class EventRepositoryTest {

    @Autowired
    private EventRepository eventRepository;

    @Test
    public void testSaveAndFindById() {
        Event event = new Event();
        event.setTitle("Spring Boot Workshop");

        // Save event to MySQL
        Event savedEvent = eventRepository.save(event);

        Optional<Event> found = eventRepository.findById(savedEvent.getEventId());
        assertTrue(found.isPresent());
        assertEquals("Spring Boot Workshop", found.get().getTitle());
    }

    @Test
    public void testSave_UpdatesExistingEvent() {
        Event event = new Event();
        event.setTitle("Initial Summit Title");
        Event savedEvent = eventRepository.save(event);

        // Update event details
        savedEvent.setTitle("Updated Summit Title");
        eventRepository.save(savedEvent);

        Optional<Event> found = eventRepository.findById(savedEvent.getEventId());
        assertTrue(found.isPresent());
        assertEquals("Updated Summit Title", found.get().getTitle());
    }

    @Test
    public void testFindByOrganizerId() {
        Event event = new Event();
        event.setOrganizerId("ORG-007");
        event.setTitle("Organizer Exclusive Event");

        eventRepository.save(event);

        List<Event> results = eventRepository.findByOrganizerId("ORG-007");
        assertFalse(results.isEmpty());
        assertEquals("ORG-007", results.get(0).getOrganizerId());
    }

    @Test
    public void testDeleteById() {
        Event event = new Event();
        event.setTitle("Event To Be Deleted");

        Event savedEvent = eventRepository.save(event);
        Long id = savedEvent.getEventId();

        assertTrue(eventRepository.findById(id).isPresent());

        eventRepository.deleteById(id);
        assertTrue(eventRepository.findById(id).isEmpty());
    }

    @Test
    public void testNullInputs_ThrowsExceptionInJpa() {
        // Spring Data JPA throws IllegalArgumentException when searching by null ID
        assertThrows(org.springframework.dao.InvalidDataAccessApiUsageException.class, () -> {
            eventRepository.findById(null);
        });
    }
}