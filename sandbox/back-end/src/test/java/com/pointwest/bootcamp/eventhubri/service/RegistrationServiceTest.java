package com.pointwest.bootcamp.eventhubri.service;

import com.pointwest.bootcamp.eventhubri.model.Event;
import com.pointwest.bootcamp.eventhubri.model.Registration;
import com.pointwest.bootcamp.eventhubri.model.RegistrationStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class RegistrationServiceTest {

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private OrganizerService organizerService;

    @Autowired
    private AttendeeService attendeeService; // <-- Inject AttendeeService

    @Test
    public void testRegisterAttendee() {
        Event event = new Event();
        event.setTitle("Hackathon 2026");
        Event createdEvent = organizerService.createEvent(event);

        Registration registration = registrationService.registerAttendee("ATT-301", createdEvent.getEventId(), "Vegetarian");

        assertNotNull(registration);
        assertNotNull(registration.getRegistrationId());
        assertEquals("ATT-301", registration.getAttendeeId());
        assertEquals(createdEvent.getEventId(), registration.getEventId());
        assertEquals(RegistrationStatus.CONFIRMED, registration.getStatus());
    }

    @Test
    public void testCancelRegistration() {
        Event event = new Event();
        event.setTitle("Design Sprint");
        Event createdEvent = organizerService.createEvent(event);

        Registration registration = registrationService.registerAttendee("ATT-302", createdEvent.getEventId(), "Gluten Free");
        assertNotNull(registration);

        registrationService.cancelRegistration(registration.getRegistrationId());

        // Use attendeeService.getMyRegistrations(...) instead
        List<Registration> attendeeRegistrations = attendeeService.getMyRegistrations("ATT-302");
        Registration cancelledReg = attendeeRegistrations.stream()
                .filter(r -> r.getRegistrationId().equals(registration.getRegistrationId()))
                .findFirst()
                .orElse(null);

        assertNotNull(cancelledReg);
        assertEquals(RegistrationStatus.CANCELLED, cancelledReg.getStatus());
    }

    @Test
    public void testSelectSessions() {
        Event event = new Event();
        event.setTitle("UX Workshop");
        Event createdEvent = organizerService.createEvent(event);

        Registration registration = registrationService.registerAttendee("ATT-303", createdEvent.getEventId(), "None");
        
        registrationService.selectSessions(registration.getRegistrationId(), Arrays.asList("SES-101", "SES-102"));
    }
}