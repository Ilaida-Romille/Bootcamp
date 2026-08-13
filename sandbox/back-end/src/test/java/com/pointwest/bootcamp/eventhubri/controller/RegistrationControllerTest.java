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
        assertEquals(4, summary.getCount());     // 4 sessions have attendee selections
        assertEquals(7, summary.getSum());       // 7 total selections across all sessions
        assertEquals(1.75, summary.getAverage()); // Average 1.75 selections per session
        assertEquals(1, summary.getMin());       // Minimum selections count = 1
        assertEquals(3, summary.getMax());       // Maximum selections count = 3

        assertEquals(4, summary.getSessionDetails().size());
    }
}