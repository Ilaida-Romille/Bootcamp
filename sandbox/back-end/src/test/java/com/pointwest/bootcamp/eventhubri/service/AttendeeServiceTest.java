package com.pointwest.bootcamp.eventhubri.service;

import com.pointwest.bootcamp.eventhubri.model.Agenda;
import com.pointwest.bootcamp.eventhubri.model.Event;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AttendeeServiceTest {

    @Autowired
    private AttendeeService attendeeService;

    @Autowired
    private OrganizerService organizerService;

    @Test
    public void testGetAllAvailableEvents() {
        Event event = new Event();
        event.setTitle("Public Bootcamp Showcase");
        organizerService.createEvent(event);

        List<Event> availableEvents = attendeeService.getAllAvailableEvents();
        assertNotNull(availableEvents);
        assertFalse(availableEvents.isEmpty());
    }

    @Test
    public void testGetEventDetailsAndAgenda() {
        Event event = new Event();
        event.setTitle("Cloud Architecture Conference");
        Event createdEvent = organizerService.createEvent(event);

        Event retrievedDetails = attendeeService.getEventDetails(createdEvent.getEventId());
        assertEquals("Cloud Architecture Conference", retrievedDetails.getTitle());

        Agenda agenda = new Agenda();
        agenda.setDescription("Track A");
        organizerService.attachAgendaToEvent(createdEvent.getEventId(), agenda);

        Agenda retrievedAgenda = attendeeService.getEventAgenda(createdEvent.getEventId());
        assertNotNull(retrievedAgenda);
        assertEquals("Track A", retrievedAgenda.getDescription());
    }

    @Test
    public void testGetEventDetails_NotFound_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            attendeeService.getEventDetails("NON_EXISTENT_ID");
        });
    }
}