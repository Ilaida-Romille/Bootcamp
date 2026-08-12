package com.pointwest.bootcamp.eventhubri;

import com.pointwest.bootcamp.eventhubri.controller.AttendeeController;
import com.pointwest.bootcamp.eventhubri.controller.OrganizerController;
import com.pointwest.bootcamp.eventhubri.controller.RegistrationController;
import com.pointwest.bootcamp.eventhubri.dto.AgendaDto;
import com.pointwest.bootcamp.eventhubri.dto.EventDto;
import com.pointwest.bootcamp.eventhubri.dto.RegistrationDto;
import com.pointwest.bootcamp.eventhubri.model.Agenda;
import com.pointwest.bootcamp.eventhubri.model.Event;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AttendeeControllerTest {

    @Autowired
    private AttendeeController attendeeController;

    @Autowired
    private OrganizerController organizerController;

    @Autowired
    private RegistrationController registrationController;

    @Test
    public void testBrowseAllEvents() {
        Event event = new Event();
        event.setTitle("Public Webinar");
        organizerController.createEvent(event);

        List<EventDto> availableEvents = attendeeController.browseAllEvents();
        assertNotNull(availableEvents);
        assertFalse(availableEvents.isEmpty());
    }

    @Test
    public void testViewEventDetailsAndAgenda() {
        Event event = new Event();
        event.setTitle("AI Horizons");
        EventDto createdEvent = organizerController.createEvent(event);

        EventDto eventDetails = attendeeController.viewEventDetails(createdEvent.getEventId());
        assertEquals("AI Horizons", eventDetails.getTitle());

        Agenda agenda = new Agenda();
        agenda.setDescription("AI Track");
        organizerController.attachAgenda(createdEvent.getEventId(), agenda);

        AgendaDto viewedAgenda = attendeeController.viewEventAgenda(createdEvent.getEventId());
        assertNotNull(viewedAgenda);
    }

    @Test
    public void testViewMyRegistrations() {
        Event event = new Event();
        event.setTitle("Community Meetup");
        EventDto createdEvent = organizerController.createEvent(event);

        registrationController.registerAttendee("ATT-200", createdEvent.getEventId(), "Halal");

        List<RegistrationDto> myRegistrations = attendeeController.viewMyRegistrations("ATT-200");
        assertNotNull(myRegistrations);
        assertFalse(myRegistrations.isEmpty());
        assertEquals("ATT-200", myRegistrations.get(0).getAttendeeId());
    }
}