package com.pointwest.bootcamp.eventhubri.modules.auth.repository;

import com.pointwest.bootcamp.eventhubri.modules.auth.entity.RefreshToken;

import jakarta.persistence.LockModeType;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository
        extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByTokenHash(String tokenHash);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
                select r
                from RefreshToken r
                where r.tokenHash = :tokenHash
            """)
    Optional<RefreshToken> findByTokenHashForUpdate(
            @Param("tokenHash") String tokenHash);

    @Modifying
    @Query("""
                update RefreshToken r
                set r.revokedAt = CURRENT_TIMESTAMP
                where r.familyId = :familyId
                  and r.revokedAt is null
            """)
    void revokeFamily(@Param("familyId") UUID familyId);

    @Modifying
    @Query("""
                update RefreshToken r
                set r.revokedAt = CURRENT_TIMESTAMP
                where r.user.id = :userId
                  and r.revokedAt is null
            """)
    void revokeAllForUser(@Param("userId") Long userId);
}