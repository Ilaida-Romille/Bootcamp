package com.pointwest.bootcamp.eventhubri.identity.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.pointwest.bootcamp.eventhubri.identity.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailIgnoreCase(String email);
    boolean existsByEmailIgnoreCase(String email);
}
