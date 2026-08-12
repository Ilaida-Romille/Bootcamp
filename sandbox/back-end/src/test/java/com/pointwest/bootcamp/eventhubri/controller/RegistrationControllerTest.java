package com.pointwest.bootcamp.eventhubri;

import com.pointwest.bootcamp.eventhubri.controller.RegistrationController;
import com.pointwest.bootcamp.eventhubri.dto.RegistrationDto;
import com.pointwest.bootcamp.eventhubri.dto.RegistrationStatusDto;
import com.pointwest.bootcamp.eventhubri.model.EventStatus;
import com.pointwest.bootcamp.eventhubri.repository.EventRepository;

import com.pointwest.bootcamp.eventhubri.model.Event;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class RegistrationControllerTest {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private RegistrationController registrationController;

    private Long testEventId;

    @BeforeEach
    void setUp() {
        // Clear previous test events to prevent state leakage
        eventRepository.deleteAll();

        // Allow JPA to auto-generate the ID cleanly
        Event event = new Event();
        event.setTitle("Tech Summit 2026");
        event.setDescription("Annual developer conference");
        event.setStatus(EventStatus.PUBLISHED);
        event.setCapacity(500);
        event.setStartDateTime(new Date());
        event.setEndDateTime(new Date(System.currentTimeMillis() + 86400000L));

        Event savedEvent = eventRepository.save(event);
        this.testEventId = savedEvent.getEventId(); // Store the generated ID
    }

    @Test
    public void testRegisterAttendee() {
        RegistrationDto registration = registrationController.registerAttendee("ATT-001", testEventId, "Vegan");

        assertNotNull(registration);
        assertNotNull(registration.getRegistrationId());
        assertEquals("ATT-001", registration.getAttendeeId());
        assertEquals(testEventId, registration.getEventId());
        assertEquals(RegistrationStatusDto.CONFIRMED, registration.getStatus());
    }

    @Test
    public void testCancelRegistration() {
        RegistrationDto registration = registrationController.registerAttendee("ATT-002", testEventId, "None");
        assertNotNull(registration);

        registrationController.cancelRegistration(registration.getRegistrationId());
    }

    @Test
    public void testSelectSessions() {
        RegistrationDto registration = registrationController.registerAttendee("ATT-003", testEventId, "Nut Allergy");
        assertNotNull(registration);

        registrationController.selectSessions(registration.getRegistrationId(), Arrays.asList("SES-001", "SES-002"));
    }
}