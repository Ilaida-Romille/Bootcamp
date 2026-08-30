package com.pointwest.bootcamp.eventhubri.modules.registration.repository;

import com.pointwest.bootcamp.eventhubri.modules.registration.entity.Registration;
import com.pointwest.bootcamp.eventhubri.modules.registration.entity.RegistrationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {

    boolean existsByEvent_IdAndAttendee_Id(Long eventId, Long attendeeId);

    Optional<Registration> findByIdAndAttendee_Id(Long id, Long attendeeId);

    Page<Registration> findByAttendee_Id(Long attendeeId, Pageable pageable);

    Page<Registration> findByEvent_Id(Long eventId, Pageable pageable);

    Page<Registration> findByEvent_IdAndStatus(Long eventId, RegistrationStatus status, Pageable pageable);

    long countByEvent_IdAndStatus(Long eventId, RegistrationStatus status);

    Optional<Registration> findFirstByEvent_IdAndStatusOrderByCreatedAtAsc(Long eventId, RegistrationStatus status);
}
