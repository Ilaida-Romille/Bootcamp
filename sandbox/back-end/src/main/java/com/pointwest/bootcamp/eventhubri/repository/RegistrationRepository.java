package com.pointwest.bootcamp.eventhubri.repository;

import com.pointwest.bootcamp.eventhubri.dto.SessionAttendeeCountDto;
import com.pointwest.bootcamp.eventhubri.model.Registration;
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
}
