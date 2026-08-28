package com.pointwest.bootcamp.eventhubri.modules.account.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.*;

import com.pointwest.bootcamp.eventhubri.modules.account.entity.AppUser;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByEmail(String email);

    Optional<AppUser> findByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCase(String email);

    Optional<AppUser> findById(Long id);
}
