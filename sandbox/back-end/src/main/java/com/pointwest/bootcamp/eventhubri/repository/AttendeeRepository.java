package com.pointwest.bootcamp.eventhubri.repository;

import com.pointwest.bootcamp.eventhubri.model.Attendee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AttendeeRepository extends JpaRepository<Attendee, String> {
    
    // Spring Data JPA inherits 'name' from the superclass User
    List<Attendee> findByNameContainingIgnoreCase(String name);
}