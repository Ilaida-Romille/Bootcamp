package com.pointwest.bootcamp.eventhubri.modules.registration.repository;

import com.pointwest.bootcamp.eventhubri.modules.registration.entity.Registration;
import com.pointwest.bootcamp.eventhubri.modules.registration.entity.RegistrationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {

    boolean existsByEvent_IdAndAttendee_Id(Long eventId, Long attendeeId);

    Optional<Registration> findByIdAndAttendee_Id(Long id, Long attendeeId);

    void deleteByAttendee_Id(Long attendeeId);

    Page<Registration> findByAttendee_Id(Long attendeeId, Pageable pageable);

    List<Registration> findByEvent_Id(Long eventId);

    Page<Registration> findByEvent_Id(Long eventId, Pageable pageable);

    Optional<Registration> findByEvent_IdAndAttendee_Id(Long eventId, Long attendeeId);

    Page<Registration> findByEvent_IdAndStatus(Long eventId, RegistrationStatus status, Pageable pageable);

    long countByEvent_IdAndStatus(Long eventId, RegistrationStatus status);

    Optional<Registration> findFirstByEvent_IdAndStatusOrderByCreatedAtAsc(Long eventId, RegistrationStatus status);

    List<Registration> findByAttendee_IdAndStatusNot(Long attendeeId, RegistrationStatus status);

    @Query("SELECT r.event.id FROM Registration r WHERE r.attendee.id = :attendeeId AND r.status <> 'CANCELLED'")
    Set<Long> findActiveRegisteredEventIds(@Param("attendeeId") Long attendeeId);
}
