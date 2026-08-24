package com.pointwest.bootcamp.eventhubri.repository;

import com.pointwest.bootcamp.eventhubri.model.Attendee;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Sql("/test-summary-data.sql")
public class AttendeeRepositoryTest {

    @Autowired
    private AttendeeRepository attendeeRepository;

    @Test
    public void testFindByNameContainingIgnoreCase_Success() {
        // Query for partial string 'john'
        List<Attendee> results = attendeeRepository.findByNameContainingIgnoreCase("john");

        assertNotNull(results);
        assertEquals(12, results.size());
        assertTrue(results.stream().allMatch(a -> a.getName().toLowerCase().contains("john")));
    }

    @Test
    public void testFindByNameContainingIgnoreCase_NoMatch() {
        List<Attendee> results = attendeeRepository.findByNameContainingIgnoreCase("NonExistentAttendeeName");

        assertNotNull(results);
        assertTrue(results.isEmpty());
    }
}