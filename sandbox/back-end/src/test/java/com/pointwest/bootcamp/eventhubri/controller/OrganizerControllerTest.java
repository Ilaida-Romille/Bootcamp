package com.pointwest.bootcamp.eventhubri.controller;

import com.pointwest.bootcamp.eventhubri.controller.AttendeeController;
import com.pointwest.bootcamp.eventhubri.controller.OrganizerController;
import com.pointwest.bootcamp.eventhubri.controller.RegistrationController;
import com.pointwest.bootcamp.eventhubri.dto.AgendaDto;
import com.pointwest.bootcamp.eventhubri.dto.EventDto;
import com.pointwest.bootcamp.eventhubri.dto.RegistrationDto;
import com.pointwest.bootcamp.eventhubri.dto.SessionDto;
import com.pointwest.bootcamp.eventhubri.dto.AttendeeDto;
import com.pointwest.bootcamp.eventhubri.model.Agenda;
import com.pointwest.bootcamp.eventhubri.model.Event;
import com.pointwest.bootcamp.eventhubri.model.PresentationSession;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

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

    // @Test
    // public void testUpdateEvent() {
    // Event event = new Event();
    // event.setTitle("Initial Title");
    // EventDto createdEvent = organizerController.createEvent(event);

    // Event updatedDetails = new Event();
    // updatedDetails.setTitle("Updated Title");

    // EventDto updatedEvent =
    // organizerController.updateEvent(createdEvent.getEventId(), updatedDetails);
    // assertEquals("Updated Title", updatedEvent.getTitle());
    // }

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

    @Test
    @Sql("/test-attendees-data.sql")
    public void testSearchAttendeesByName_PartialAndCaseInsensitive() {
        // Search "john" -> should return "John Doe" and "Johnny Appleseed"
        List<AttendeeDto> results = organizerController.searchAttendeesByName("john");

        assertNotNull(results);
        assertEquals(12, results.size());
        assertTrue(results.stream().anyMatch(a -> "John Doe".equals(a.getName())));
        assertTrue(results.stream().anyMatch(a -> "Johnny Appleseed".equals(a.getName())));
    }

    @Test
    @Sql("/test-attendees-data.sql")
    public void testSearchAttendeesByName_SingleResult() {
        // Search "Jane" -> should return "Jane Smith"
        List<AttendeeDto> results = organizerController.searchAttendeesByName("Jane");

        assertNotNull(results);
        assertEquals(4, results.size());
        assertEquals("Jane Smith", results.get(0).getName());
        assertEquals("ATT-002", results.get(0).getAttendeeId());
    }

    @Test
    @Sql("/test-attendees-data.sql")
    public void testSearchAttendeesByName_NoMatch() {
        // Search for non-existent name
        List<AttendeeDto> results = organizerController.searchAttendeesByName("NonExistent");

        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test
    public void testSearchAttendeesByName_NullOrBlankInput() {
        // Null search query
        List<AttendeeDto> nullResults = organizerController.searchAttendeesByName(null);
        assertNotNull(nullResults);
        assertTrue(nullResults.isEmpty());

        // Blank search query
        List<AttendeeDto> blankResults = organizerController.searchAttendeesByName("   ");
        assertNotNull(blankResults);
        assertTrue(blankResults.isEmpty());
    }
}