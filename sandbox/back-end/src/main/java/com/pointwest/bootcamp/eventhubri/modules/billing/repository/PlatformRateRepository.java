package com.pointwest.bootcamp.eventhubri.modules.billing.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pointwest.bootcamp.eventhubri.modules.billing.entity.PlatformRate;

public interface PlatformRateRepository extends JpaRepository<PlatformRate, Long> {

    Optional<PlatformRate> findByIsActiveTrue();
}