package com.pointwest.bootcamp.eventhubri;

import com.pointwest.bootcamp.eventhubri.controller.AttendeeController;
import com.pointwest.bootcamp.eventhubri.controller.OrganizerController;
import com.pointwest.bootcamp.eventhubri.controller.RegistrationController;
import com.pointwest.bootcamp.eventhubri.dto.AgendaDto;
import com.pointwest.bootcamp.eventhubri.dto.EventDto;
import com.pointwest.bootcamp.eventhubri.dto.RegistrationDto;
import com.pointwest.bootcamp.eventhubri.dto.SessionDto;
import com.pointwest.bootcamp.eventhubri.model.Agenda;
import com.pointwest.bootcamp.eventhubri.model.Event;
import com.pointwest.bootcamp.eventhubri.model.PresentationSession;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class OrganizerControllerTest {

    @Autowired
    private OrganizerController organizerController;

    @Autowired
    private AttendeeController attendeeController;

    @Autowired
    private RegistrationController registrationController;

    @Test
    public void testCreateAndGetEvent() {
        Event event = new Event();
        event.setTitle("Tech Summit 2026");
        event.setCapacity(200);

        EventDto createdEvent = organizerController.createEvent(event);

        assertNotNull(createdEvent);
        assertNotNull(createdEvent.getEventId());

        EventDto fetchedEvent = organizerController.getEvent(createdEvent.getEventId());
        assertEquals("Tech Summit 2026", fetchedEvent.getTitle());
    }

    @Test
    public void testUpdateEvent() {
        Event event = new Event();
        event.setTitle("Initial Title");
        EventDto createdEvent = organizerController.createEvent(event);

        Event updatedDetails = new Event();
        updatedDetails.setTitle("Updated Title");

        EventDto updatedEvent = organizerController.updateEvent(createdEvent.getEventId(), updatedDetails);
        assertEquals("Updated Title", updatedEvent.getTitle());
    }

    @Test
    public void testDeleteEvent() {
        Event event = new Event();
        event.setTitle("Temporary Event");
        EventDto createdEvent = organizerController.createEvent(event);

        organizerController.deleteEvent(createdEvent.getEventId());

        assertThrows(IllegalArgumentException.class, () -> {
            organizerController.getEvent(createdEvent.getEventId());
        });
    }

    @Test
    public void testAttachAgendaAndAddSession() {
        Event event = new Event();
        event.setTitle("Developer Conference");
        EventDto createdEvent = organizerController.createEvent(event);

        Agenda agenda = new Agenda();
        agenda.setDescription("Full-day tech talks");
        AgendaDto attachedAgenda = organizerController.attachAgenda(createdEvent.getEventId(), agenda);

        assertNotNull(attachedAgenda);

        PresentationSession session = new PresentationSession();
        session.setTitle("Keynote: Modern Java");
        session.setSpeaker("John Doe");

        organizerController.addSessionToAgenda(createdEvent.getEventId(), session);

        List<SessionDto> sessions = attendeeController.viewEventSessions(createdEvent.getEventId());
        assertFalse(sessions.isEmpty());
    }

    @Test
    public void testGetEventRegistrationsForOrganizer() {
        Event event = new Event();
        event.setTitle("Registration Test Event");
        EventDto createdEvent = organizerController.createEvent(event);

        registrationController.registerAttendee("ATT-100", createdEvent.getEventId(), "Standard");

        List<RegistrationDto> registrations = organizerController.getEventRegistrations(createdEvent.getEventId());
        assertNotNull(registrations);
        assertFalse(registrations.isEmpty());
        assertEquals("ATT-100", registrations.get(0).getAttendeeId());
    }
}