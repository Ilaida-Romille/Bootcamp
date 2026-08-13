package com.pointwest.bootcamp.eventhubri.repository;

import com.pointwest.bootcamp.eventhubri.model.Event;
import com.pointwest.bootcamp.eventhubri.model.Session;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByOrganizerId(String organizerId);

    @Query("SELECT DISTINCT s " +
       "FROM Event e " +
       "JOIN e.agenda a " +
       "JOIN a.sessions s " +
       "WHERE LOWER(s.title) LIKE LOWER(CONCAT('%', :title, '%'))")
List<Session> findSessionsByTitle(@Param("title") String title);
}
