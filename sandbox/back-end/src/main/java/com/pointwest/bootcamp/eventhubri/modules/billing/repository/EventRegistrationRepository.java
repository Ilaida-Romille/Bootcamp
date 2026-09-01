package com.pointwest.bootcamp.eventhubri.modules.billing.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pointwest.bootcamp.eventhubri.modules.event.entity.Event;
import com.pointwest.bootcamp.eventhubri.modules.registration.entity.Registration;

public interface EventRegistrationRepository extends JpaRepository<Registration, Long> {

        interface EventRegistrationSummary {
                Event getEvent();

                Long getRegistrationCount();
        }

        @Query("""
                        SELECT r.event AS event, COUNT(r.id) AS registrationCount
                        FROM Registration r
                        WHERE r.event.organization.id = :organizationId
                          AND r.status = com.pointwest.bootcamp.eventhubri.modules.registration.entity.RegistrationStatus.CONFIRMED
                          AND r.event.startTime BETWEEN :periodStart AND :periodEnd
                        GROUP BY r.event
                        """)
        List<EventRegistrationSummary> findConfirmedRegistrationsByEventForOrganizationAndPeriod(
                        @Param("organizationId") Long organizationId,
                        @Param("periodStart") LocalDateTime periodStart,
                        @Param("periodEnd") LocalDateTime periodEnd);
}