package com.pointwest.bootcamp.eventhubri.auth.service;

import java.time.Clock;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.pointwest.bootcamp.eventhubri.auth.exception.InvalidRefreshTokenException;
import com.pointwest.bootcamp.eventhubri.auth.exception.RefreshTokenReuseException;
import com.pointwest.bootcamp.eventhubri.identity.entity.RefreshToken;
import com.pointwest.bootcamp.eventhubri.identity.entity.User;
import com.pointwest.bootcamp.eventhubri.identity.repository.RefreshTokenRepository;
import com.pointwest.bootcamp.eventhubri.security.config.JwtProperties;
import com.pointwest.bootcamp.eventhubri.security.jwt.RefreshTokenGenerator;

@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenGenerator tokenGenerator;
    private final JwtProperties jwtProperties;
    private final Clock clock;

    public RefreshTokenService(
            RefreshTokenRepository refreshTokenRepository,
            RefreshTokenGenerator tokenGenerator,
            JwtProperties jwtProperties,
            Clock clock) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.tokenGenerator = tokenGenerator;
        this.jwtProperties = jwtProperties;
        this.clock = clock;
    }

    @Transactional
    public IssuedRefreshToken issue(User user) {
        String rawToken = tokenGenerator.generate();
        LocalDateTime expiresAt = LocalDateTime.ofInstant(
            clock.instant().plus(jwtProperties.refreshTokenTtl()), clock.getZone());
        RefreshToken entity =
            new RefreshToken(user, tokenGenerator.hash(rawToken), expiresAt);
        refreshTokenRepository.save(entity);
        return new IssuedRefreshToken(rawToken, entity);
    }

    @Transactional
    public RotatedRefreshToken rotate(String rawToken) {
        String hash = tokenGenerator.hash(rawToken);
        RefreshToken current = refreshTokenRepository.findByTokenHashForUpdate(hash)
            .orElseThrow(() -> new InvalidRefreshTokenException("Invalid refresh token"));

        if (Boolean.TRUE.equals(current.getRevoked())) {
            refreshTokenRepository.revokeAllByUserId(current.getUser().getId());
            throw new RefreshTokenReuseException(
                "Refresh token reuse detected; all active refresh tokens were revoked");
        }

        LocalDateTime now = LocalDateTime.ofInstant(clock.instant(), clock.getZone());
        if (!now.isBefore(current.getExpiresAt())) {
            current.setRevoked(true);
            refreshTokenRepository.save(current);
            throw new InvalidRefreshTokenException("Refresh token has expired");
        }

        User user = current.getUser();
        if (!Boolean.TRUE.equals(user.getIsActive())) {
            current.setRevoked(true);
            refreshTokenRepository.save(current);
            throw new InvalidRefreshTokenException("User account is inactive");
        }

        IssuedRefreshToken replacement = issue(user);
        current.setRevoked(true);
        current.setReplacedByToken(replacement.entity());
        refreshTokenRepository.save(current);

        return new RotatedRefreshToken(user, replacement.rawToken(), replacement.entity());
    }

    @Transactional
    public boolean revoke(String rawToken) {
        if (rawToken == null || rawToken.isBlank()) return false;
        String hash = tokenGenerator.hash(rawToken);
        return refreshTokenRepository.findByTokenHashForUpdate(hash)
            .map(token -> {
                token.setRevoked(true);
                refreshTokenRepository.save(token);
                return true;
            })
            .orElse(false);
    }

    public record IssuedRefreshToken(String rawToken, RefreshToken entity) {}
    public record RotatedRefreshToken(User user, String rawToken, RefreshToken entity) {}
}
