package com.pointwest.bootcamp.eventhubri.repository;

import com.pointwest.bootcamp.eventhubri.model.Attendee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendeeRepository extends JpaRepository<Attendee, String> {

    @Query("SELECT a FROM Attendee a WHERE LOWER(a.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Attendee> findByNameContainingIgnoreCase(@Param("name") String name);
}