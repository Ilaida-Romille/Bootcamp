package com.pointwest.bootcamp.eventhubri;

import com.pointwest.bootcamp.eventhubri.controller.RegistrationController;
import com.pointwest.bootcamp.eventhubri.dto.RegistrationDto;
import com.pointwest.bootcamp.eventhubri.dto.RegistrationStatusDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class RegistrationControllerTest {

    @Autowired
    private RegistrationController registrationController;

    @Test
    public void testRegisterAttendee() {
        RegistrationDto registration = registrationController.registerAttendee("ATT-001", 101L, "Vegan");

        assertNotNull(registration);
        assertNotNull(registration.getRegistrationId());
        assertEquals("ATT-001", registration.getAttendeeId());
        assertEquals(101L, registration.getEventId());
        assertEquals(RegistrationStatusDto.CONFIRMED, registration.getStatus());
    }

    @Test
    public void testCancelRegistration() {
        RegistrationDto registration = registrationController.registerAttendee("ATT-002", 101L, "None");
        assertNotNull(registration);

        registrationController.cancelRegistration(registration.getRegistrationId());
    }

    @Test
    public void testSelectSessions() {
        RegistrationDto registration = registrationController.registerAttendee("ATT-003", 101L, "Nut Allergy");
        assertNotNull(registration);

        registrationController.selectSessions(registration.getRegistrationId(), Arrays.asList("SES-001", "SES-002"));
    }
}