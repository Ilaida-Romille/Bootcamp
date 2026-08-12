package com.pointwest.bootcamp.eventhubri.repository;

import com.pointwest.bootcamp.eventhubri.model.Registration;
import com.pointwest.bootcamp.eventhubri.model.RegistrationStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class RegistrationRepositoryTest {

    @Autowired
    private RegistrationRepository registrationRepository;

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
        reg.setRegistrationId("REG-002");
        reg.setStatus(RegistrationStatus.PENDING);
        registrationRepository.save(reg);

        // Update same registration ID
        Registration updatedReg = new Registration();
        updatedReg.setRegistrationId("REG-002");
        updatedReg.setStatus(RegistrationStatus.CONFIRMED);
        registrationRepository.save(updatedReg);

        Optional<Registration> found = registrationRepository.findById("REG-002");
        assertTrue(found.isPresent());
        assertEquals(RegistrationStatus.CONFIRMED, found.get().getStatus());
    }

    @Test
    public void testFindByEventId() {
        Registration reg1 = new Registration();
        reg1.setRegistrationId("REG-003");
        reg1.setEventId(102L);

        Registration reg2 = new Registration();
        reg2.setRegistrationId("REG-004");
        reg2.setEventId(102L);

        registrationRepository.save(reg1);
        registrationRepository.save(reg2);

        List<Registration> results = registrationRepository.findByEventId(102L);
        assertFalse(results.isEmpty());
    }

    @Test
    public void testFindByAttendeeId() {
        Registration reg = new Registration();
        reg.setRegistrationId("REG-005");
        reg.setAttendeeId("ATT-500");

        registrationRepository.save(reg);

        List<Registration> results = registrationRepository.findByAttendeeId("ATT-500");
        assertFalse(results.isEmpty());
        assertEquals("REG-005", results.get(0).getRegistrationId());
    }

    @Test
    public void testNullInputs_ThrowsExceptionInJpa() {
        // Spring Data JPA throws IllegalArgumentException when searching by null ID
        assertThrows(org.springframework.dao.InvalidDataAccessApiUsageException.class, () -> {
            registrationRepository.findById(null);
        });
    }
}