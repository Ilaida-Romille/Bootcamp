package com.pointwest.bootcamp.eventhubri.modules.billing.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pointwest.bootcamp.eventhubri.modules.registration.entity.Registration;

public interface EventRegistrationRepository extends JpaRepository<Registration, Long> {

    @Query("SELECT COUNT(r) FROM Registration r "
            + "WHERE r.event.organization.id = :organizationId "
            + "AND r.status = com.pointwest.bootcamp.eventhubri.modules.registration.entity.RegistrationStatus.CONFIRMED "
            + "AND r.event.startTime BETWEEN :periodStart AND :periodEnd")
    long countConfirmedRegistrationsForOrganization(
            @Param("organizationId") Long organizationId,
            @Param("periodStart") LocalDateTime periodStart,
            @Param("periodEnd") LocalDateTime periodEnd);
}