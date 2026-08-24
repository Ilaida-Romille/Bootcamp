package com.pointwest.bootcamp.eventhubri.service;

import com.pointwest.bootcamp.eventhubri.model.Agenda;
import com.pointwest.bootcamp.eventhubri.model.Attendee;
import com.pointwest.bootcamp.eventhubri.model.Event;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Sql("/test-summary-data.sql")
public class AttendeeServiceTest {

    @Autowired
    private AttendeeService attendeeService;

    @Autowired
    private OrganizerService organizerService;

    @Test
    public void testGetAllAvailableEvents() {
        List<Event> availableEvents = attendeeService.getAllAvailableEvents();
        assertNotNull(availableEvents);
        assertFalse(availableEvents.isEmpty());
    }

    @Test
    public void testGetEventDetailsAndAgenda() {
        Event retrievedDetails = attendeeService.getEventDetails(1000L);
        assertNotNull(retrievedDetails);
        assertEquals("Tech Summit 2026", retrievedDetails.getTitle());

        Agenda retrievedAgenda = attendeeService.getEventAgenda(1000L);
        assertNotNull(retrievedAgenda);
        assertEquals("Tech Summit Main Agenda", retrievedAgenda.getDescription());
    }

    @Test
    public void testGetEventDetails_NotFound_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            attendeeService.getEventDetails(999999L);
        });
    }

    @Test
    public void testGetMyRegistrations() {
        var registrations = attendeeService.getMyRegistrations("ATT-001");
        assertNotNull(registrations);
        assertFalse(registrations.isEmpty());
        assertEquals("ATT-001", registrations.get(0).getAttendeeId());
    }
}