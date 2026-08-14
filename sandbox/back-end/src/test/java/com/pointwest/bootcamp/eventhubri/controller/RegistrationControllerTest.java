package com.pointwest.bootcamp.eventhubri;

import com.pointwest.bootcamp.eventhubri.controller.RegistrationController;
import com.pointwest.bootcamp.eventhubri.dto.RegistrationDto;
import com.pointwest.bootcamp.eventhubri.dto.RegistrationStatusDto;
import com.pointwest.bootcamp.eventhubri.dto.SessionPopularitySummaryDto;
import com.pointwest.bootcamp.eventhubri.model.*;
import com.pointwest.bootcamp.eventhubri.repository.EventRepository;
import com.pointwest.bootcamp.eventhubri.repository.RegistrationRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Sql("/test-summary-data.sql")
public class RegistrationControllerTest {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private RegistrationController registrationController;

    private final Long TEST_EVENT_ID_1 = 1000L;
    // private Long TEST_EVENT_ID_1;

    // @BeforeEach
    // void setUp() {
    //     // Clear previous test events to prevent state leakage
    //     eventRepository.deleteAll();

    //     // Allow JPA to auto-generate the ID cleanly
    //     Event event = new Event();
    //     event.setTitle("Tech Summit 2026");
    //     event.setDescription("Annual developer conference");
    //     event.setStatus(EventStatus.PUBLISHED);
    //     event.setCapacity(500);
    //     event.setStartDateTime(new Date());
    //     event.setEndDateTime(new Date(System.currentTimeMillis() + 86400000L));

    //     Event savedEvent = eventRepository.save(event);
    //     this.TEST_EVENT_ID_1 = savedEvent.getEventId(); // Store the generated ID
    // }

    @Test
    public void testRegisterAttendee() {
        RegistrationDto registration = registrationController.registerAttendee("ATT-003", TEST_EVENT_ID_1, "Vegan");

        assertNotNull(registration);
        assertNotNull(registration.getRegistrationId());
        assertEquals("ATT-003", registration.getAttendeeId());
        assertEquals(TEST_EVENT_ID_1, registration.getEventId());
        assertEquals(RegistrationStatusDto.CONFIRMED, registration.getStatus());
    }

    @Test
    public void testCancelRegistration() {
        RegistrationDto registration = registrationController.registerAttendee("ATT-004", TEST_EVENT_ID_1, "None");
        assertNotNull(registration);

        registrationController.cancelRegistration(registration.getRegistrationId());
    }

    @Test
    public void testSelectSessions() {
        RegistrationDto registration = registrationController.registerAttendee("ATT-005", TEST_EVENT_ID_1, "Nut Allergy");
        assertNotNull(registration);

        registrationController.selectSessions(registration.getRegistrationId(), Arrays.asList("SES-001", "SES-002"));
    }

    @Test
    public void testGetSessionPopularitySummary() {
        SessionPopularitySummaryDto summary = registrationController.getSessionPopularitySummary(TEST_EVENT_ID_1);

        assertNotNull(summary);
        assertEquals(TEST_EVENT_ID_1, summary.getEventId());
        
        // Summary calculations for 100 selections across 5 sessions:
        assertEquals(5, summary.getCount());     // All 5 sessions have selections
        assertEquals(100, summary.getSum());     // 100 total selections
        assertEquals(20.0, summary.getAverage()); // Average 100 / 5 = 20.0 per session
        assertEquals(5, summary.getMin());       // Minimum count = 5 (SES-105)
        assertEquals(40, summary.getMax());      // Maximum count = 40 (SES-101)

        assertEquals(5, summary.getSessionDetails().size());
    }

    @Test
    public void testSearchSessionsByTitle_Success() {
        List<Session> results = registrationController.searchSessionsByTitle("Keynote");

        assertNotNull(results);
        assertEquals(2, results.size());
        assertTrue(results.stream().anyMatch(s -> s.getTitle().equals("Keynote: Java 21")));
        assertTrue(results.stream().anyMatch(s -> s.getTitle().equals("AI Keynote")));
    }

    @Test
    public void testSearchSessionsByTitle_CaseInsensitive() {
        List<Session> results = registrationController.searchSessionsByTitle("spring");

        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("SES-103", results.get(0).getSessionId());
        assertEquals("Spring Boot 3 Deep Dive", results.get(0).getTitle());
    }

    @Test
    public void testSearchSessionsByTitle_NoMatch() {
        List<Session> results = registrationController.searchSessionsByTitle("NonExistentSessionTitle");

        assertNotNull(results);
        assertTrue(results.isEmpty());
    }
}