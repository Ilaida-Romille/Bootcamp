package com.pointwest.bootcamp.eventhubri.modules.billing.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pointwest.bootcamp.eventhubri.modules.billing.entity.PlatformRate;

public interface PlatformRateRepository extends JpaRepository<PlatformRate, Long> {

    Optional<PlatformRate> findByIsActiveTrue();

    @Query("""
            SELECT pr
            FROM PlatformRate pr
            WHERE pr.effectiveStartDate <= :periodEnd
              AND (pr.effectiveEndDate IS NULL OR pr.effectiveEndDate >= :periodStart)
            ORDER BY pr.effectiveStartDate DESC, pr.id DESC
            """)
    Optional<PlatformRate> findApplicableRateForPeriod(
            @Param("periodStart") LocalDate periodStart,
            @Param("periodEnd") LocalDate periodEnd);
}