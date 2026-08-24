package com.pointwest.bootcamp.eventhubri.controller;

import com.pointwest.bootcamp.eventhubri.controller.AttendeeController;
import com.pointwest.bootcamp.eventhubri.dto.AgendaDto;
import com.pointwest.bootcamp.eventhubri.dto.EventDto;
import com.pointwest.bootcamp.eventhubri.dto.RegistrationDto;
import com.pointwest.bootcamp.eventhubri.dto.SessionDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Sql("/test-summary-data.sql")
public class AttendeeControllerTest {

    @Autowired
    private AttendeeController attendeeController;

    @Test
    public void testBrowseAllEvents() {
        List<EventDto> availableEvents = attendeeController.browseAllEvents();
        assertNotNull(availableEvents);
        assertEquals(2, availableEvents.size());
    }

    @Test
    public void testViewEventDetailsAgendaAndSessions() {
        // Event details
        EventDto eventDetails = attendeeController.viewEventDetails(1000L);
        assertNotNull(eventDetails);
        assertEquals("Tech Summit 2026", eventDetails.getTitle());

        // Event agenda
        AgendaDto viewedAgenda = attendeeController.viewEventAgenda(1000L);
        assertNotNull(viewedAgenda);
        assertEquals("Tech Summit Main Agenda", viewedAgenda.getDescription());

        // Event sessions
        List<SessionDto> sessions = attendeeController.viewEventSessions(1000L);
        assertNotNull(sessions);
        assertEquals(5, sessions.size());
    }

    @Test
    public void testViewMyRegistrations() {
        List<RegistrationDto> myRegistrations = attendeeController.viewMyRegistrations("ATT-001");
        assertNotNull(myRegistrations);
        assertFalse(myRegistrations.isEmpty());
        assertEquals("ATT-001", myRegistrations.get(0).getAttendeeId());
    }
}