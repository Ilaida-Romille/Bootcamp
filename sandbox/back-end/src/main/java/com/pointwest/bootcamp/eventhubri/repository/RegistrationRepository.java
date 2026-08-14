package com.pointwest.bootcamp.eventhubri.repository;

import com.pointwest.bootcamp.eventhubri.dto.SessionAttendeeCountDto;
import com.pointwest.bootcamp.eventhubri.dto.SessionPopularityStatsProjectionDto;
import com.pointwest.bootcamp.eventhubri.model.Registration;
import com.pointwest.bootcamp.eventhubri.model.Session;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

public interface RegistrationRepository extends JpaRepository<Registration, String>{
    List<Registration> findByEventId(Long eventId);

    List<Registration> findByAttendeeId(String attendeeId);

    @Query("SELECT new com.pointwest.bootcamp.eventhubri.dto.SessionAttendeeCountDto(" +
           "  s.sessionId, s.title, COUNT(r.registrationId)) " +
           "FROM Registration r " +
           "JOIN r.sessionSelections ss " +
           "JOIN ss.session s " +
           "WHERE r.eventId = :eventId " +
           "GROUP BY s.sessionId, s.title")
    List<SessionAttendeeCountDto> countAttendeesPerSessionByEventId(@Param("eventId") Long eventId);

    @Query(value = "SELECT " +
           "  COUNT(*) AS count, " +
           "  COALESCE(SUM(sub.cnt), 0) AS sum, " +
           "  COALESCE(AVG(sub.cnt), 0.0) AS average, " +
           "  COALESCE(MIN(sub.cnt), 0) AS min, " +
           "  COALESCE(MAX(sub.cnt), 0) AS max " +
           "FROM (" +
           "  SELECT COUNT(ss.selection_id) AS cnt " +
           "  FROM registrations r " +
           "  JOIN session_selections ss ON r.registration_id = ss.registration_id " +
           "  WHERE r.event_id = :eventId " +
           "  GROUP BY ss.session_id" +
           ") sub", nativeQuery = true)
    SessionPopularityStatsProjectionDto getSessionPopularityStatsByEventId(@Param("eventId") Long eventId);
}
