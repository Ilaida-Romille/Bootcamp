package com.pointwest.bootcamp.eventhubri.service;

import com.pointwest.bootcamp.eventhubri.model.Agenda;
import com.pointwest.bootcamp.eventhubri.model.Event;
import com.pointwest.bootcamp.eventhubri.model.PresentationSession;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class OrganizerServiceTest {

    @Autowired
    private OrganizerService organizerService;

    @Test
    public void testCreateAndGetEvent() {
        Event event = new Event();
        event.setTitle("AI Summit 2026");
        event.setCapacity(150);

        Event createdEvent = organizerService.createEvent(event);

        assertNotNull(createdEvent);
        assertNotNull(createdEvent.getEventId());

        Event fetchedEvent = organizerService.getEventById(createdEvent.getEventId());
        assertEquals("AI Summit 2026", fetchedEvent.getTitle());
    }

    @Test
    public void testUpdateEvent() {
        Event event = new Event();
        event.setTitle("Original Title");
        Event createdEvent = organizerService.createEvent(event);

        Event updatedDetails = new Event();
        updatedDetails.setTitle("Updated Title");

        Event updatedEvent = organizerService.updateEvent(createdEvent.getEventId(), updatedDetails);
        assertEquals("Updated Title", updatedEvent.getTitle());
    }

    @Test
    public void testDeleteEvent() {
        Event event = new Event();
        event.setTitle("Event to Delete");
        Event createdEvent = organizerService.createEvent(event);

        organizerService.deleteEvent(createdEvent.getEventId());

        assertThrows(IllegalArgumentException.class, () -> {
            organizerService.getEventById(createdEvent.getEventId());
        });
    }

    @Test
    public void testAttachAgendaAndAddSession() {
        Event event = new Event();
        event.setTitle("Workshop Event");
        Event createdEvent = organizerService.createEvent(event);

        Agenda agenda = new Agenda();
        agenda.setDescription("Morning Keynotes");
        Agenda attachedAgenda = organizerService.attachAgendaToEvent(createdEvent.getEventId(), agenda);

        assertNotNull(attachedAgenda);
        assertNotNull(attachedAgenda.getAgendaId());

        PresentationSession session = new PresentationSession();
        session.setTitle("Spring Boot Deep Dive");
        session.setSpeaker("Jane Doe");

        organizerService.addSessionToEventAgenda(createdEvent.getEventId(), session);

        Event updatedEvent = organizerService.getEventById(createdEvent.getEventId());
        assertNotNull(updatedEvent.getAgenda());
        assertFalse(updatedEvent.getAgenda().getSessions().isEmpty());
    }
}