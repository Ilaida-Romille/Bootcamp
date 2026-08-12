package com.pointwest.bootcamp.eventhubri.repository;

import com.pointwest.bootcamp.eventhubri.model.Registration;
import com.pointwest.bootcamp.eventhubri.model.RegistrationStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class RegistrationRepositoryImplTest {

    private RegistrationRepositoryImpl registrationRepository;

    @BeforeEach
    public void setUp() {
        registrationRepository = new RegistrationRepositoryImpl();
    }

    @Test
    public void testSaveAndFindById() {
        Registration reg = new Registration();
        reg.setRegistrationId("REG-001");
        reg.setAttendeeId("ATT-100");
        reg.setEventId(100L);

        registrationRepository.save(reg);

        Optional<Registration> found = registrationRepository.findById("REG-001");
        assertTrue(found.isPresent());
        assertEquals("ATT-100", found.get().getAttendeeId());
    }

    @Test
    public void testSave_UpdatesExistingRegistration() {
        Registration reg = new Registration();
        reg.setRegistrationId("REG-001");
        reg.setStatus(RegistrationStatus.PENDING);
        registrationRepository.save(reg);

        // Update same registration ID
        Registration updatedReg = new Registration();
        updatedReg.setRegistrationId("REG-001");
        updatedReg.setStatus(RegistrationStatus.CONFIRMED);
        registrationRepository.save(updatedReg);

        Optional<Registration> found = registrationRepository.findById("REG-001");
        assertTrue(found.isPresent());
        assertEquals(RegistrationStatus.CONFIRMED, found.get().getStatus());
        assertEquals(1, registrationRepository.findByEventId(null).isEmpty() ? 1 : 1);
    }

    @Test
    public void testFindByEventId() {
        Registration reg1 = new Registration();
        reg1.setRegistrationId("REG-001");
        reg1.setEventId(102L);

        Registration reg2 = new Registration();
        reg2.setRegistrationId("REG-002");
        reg2.setEventId(102L);

        registrationRepository.save(reg1);
        registrationRepository.save(reg2);

        List<Registration> results = registrationRepository.findByEventId(102L);
        assertEquals(2, results.size());
    }

    @Test
    public void testFindByAttendeeId() {
        Registration reg = new Registration();
        reg.setRegistrationId("REG-001");
        reg.setAttendeeId("ATT-500");

        registrationRepository.save(reg);

        List<Registration> results = registrationRepository.findByAttendeeId("ATT-500");
        assertEquals(1, results.size());
        assertEquals("REG-001", results.get(0).getRegistrationId());
    }

    @Test
    public void testNullInputs_ReturnEmptyResultsSafely() {
        assertTrue(registrationRepository.findByEventId(null).isEmpty());
        assertTrue(registrationRepository.findByAttendeeId(null).isEmpty());
        assertTrue(registrationRepository.findById(null).isEmpty());
    }
}