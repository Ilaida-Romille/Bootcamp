package com.pointwest.bootcamp.eventhubri.modules.auth.service;

import com.pointwest.bootcamp.eventhubri.modules.account.entity.AppUser;
import com.pointwest.bootcamp.eventhubri.modules.account.entity.Organization;
import com.pointwest.bootcamp.eventhubri.modules.account.entity.Role;
import com.pointwest.bootcamp.eventhubri.modules.account.repository.AppUserRepository;
import com.pointwest.bootcamp.eventhubri.modules.account.repository.OrganizationRepository;
import com.pointwest.bootcamp.eventhubri.modules.auth.dto.*;
import com.pointwest.bootcamp.eventhubri.modules.auth.entity.RefreshToken;
import com.pointwest.bootcamp.eventhubri.modules.auth.repository.RefreshTokenRepository;
import com.pointwest.bootcamp.eventhubri.modules.auth.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

        private final AppUserRepository appUserRepository;
        private final OrganizationRepository organizationRepository;
        private final RefreshTokenRepository refreshTokenRepository;
        private final PasswordEncoder passwordEncoder;
        private final JwtService jwtService;

        private final SecureRandom secureRandom = new SecureRandom();

        private static final long REFRESH_TOKEN_DAYS = 30;

        @Transactional
        public void registerOrganizer(
                        RegisterOrganizerRequestDto request) {

                if (appUserRepository
                                .existsByEmailIgnoreCase(request.email())) {

                        throw new IllegalArgumentException(
                                        "An account with this email already exists");
                }

                if (organizationRepository
                                .existsByPrimaryContactEmailIgnoreCase(
                                                request.primaryContactEmail())) {

                        throw new IllegalArgumentException(
                                        "An organization with this email already exists");
                }

                Organization organization = Organization.builder()
                                .companyName(request.companyName())
                                .primaryContactEmail(
                                                request.primaryContactEmail())
                                .primaryContactPhone(
                                                request.primaryContactPhone())
                                .status(
                                                Organization.Status.PENDING)
                                .build();

                organization = organizationRepository.save(organization);

                AppUser user = AppUser.builder()
                                .organization(organization)
                                .email(request.email())
                                .passwordHash(
                                                passwordEncoder.encode(
                                                                request.password()))
                                .firstName(request.firstName())
                                .lastName(request.lastName())
                                .role(Role.ORGANIZER_ADMIN)
                                .status(AppUser.Status.ACTIVE)
                                .build();

                appUserRepository.save(user);
        }

        @Transactional
        public AuthResult login(
                        LoginRequestDto request) {

                AppUser user = appUserRepository
                                .findByEmailIgnoreCase(
                                                request.email())
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "Invalid email or password"));

                if (user.getStatus() != AppUser.Status.ACTIVE) {
                        throw new IllegalStateException(
                                        "User account is inactive");
                }

                if (!passwordEncoder.matches(
                                request.password(),
                                user.getPasswordHash())) {
                        throw new IllegalArgumentException(
                                        "Invalid email or password");
                }

                String accessToken = jwtService.generateAccessToken(user);

                RefreshTokenPair refreshTokenPair = createRefreshToken(user);

                return new AuthResult(
                                accessToken,
                                jwtService.getExpirationSeconds(),
                                refreshTokenPair.rawToken());
        }

        @Transactional
        public AuthResult refresh(
                        String rawRefreshToken) {

                String tokenHash = hashToken(rawRefreshToken);

                RefreshToken current = refreshTokenRepository
                                .findByTokenHashForUpdate(tokenHash)
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "Invalid refresh token"));

                /*
                 * Reuse detection.
                 */
                if (current.isUsed() || current.isRevoked()) {

                        refreshTokenRepository.revokeFamily(
                                        current.getFamilyId());

                        throw new IllegalArgumentException(
                                        "Refresh token reuse detected");
                }

                if (current.isExpired()) {

                        current.setRevokedAt(
                                        LocalDateTime.now());

                        throw new IllegalArgumentException(
                                        "Refresh token has expired");
                }

                AppUser user = current.getUser();

                if (user.getStatus() != AppUser.Status.ACTIVE) {

                        refreshTokenRepository.revokeFamily(
                                        current.getFamilyId());

                        throw new IllegalStateException(
                                        "User account is inactive");
                }

                current.setUsedAt(
                                LocalDateTime.now());

                String accessToken = jwtService.generateAccessToken(user);

                RefreshTokenPair replacement = createRefreshToken(
                                user,
                                current.getFamilyId());

                current.setReplacedByTokenId(
                                replacement.entity().getId());

                refreshTokenRepository.save(current);

                return new AuthResult(
                                accessToken,
                                jwtService.getExpirationSeconds(),
                                replacement.rawToken());
        }

        @Transactional
        public void logout(String rawRefreshToken) {

                if (rawRefreshToken == null ||
                                rawRefreshToken.isBlank()) {
                        return;
                }

                refreshTokenRepository
                                .findByTokenHash(
                                                hashToken(rawRefreshToken))
                                .ifPresent(token -> {

                                        refreshTokenRepository.revokeFamily(
                                                        token.getFamilyId());
                                });
        }

        private RefreshTokenPair createRefreshToken(
                        AppUser user) {
                return createRefreshToken(
                                user,
                                UUID.randomUUID());
        }

        private RefreshTokenPair createRefreshToken(
                        AppUser user,
                        UUID familyId) {

                String rawToken = generateRandomToken();

                RefreshToken token = RefreshToken.builder()
                                .user(user)
                                .tokenHash(hashToken(rawToken))
                                .familyId(familyId)
                                .expiresAt(
                                                LocalDateTime.now()
                                                                .plusDays(
                                                                                REFRESH_TOKEN_DAYS))
                                .build();

                RefreshToken saved = refreshTokenRepository.save(token);

                return new RefreshTokenPair(
                                rawToken,
                                saved);
        }

        private String generateRandomToken() {

                byte[] bytes = new byte[32];

                secureRandom.nextBytes(bytes);

                return Base64.getUrlEncoder()
                                .withoutPadding()
                                .encodeToString(bytes);
        }

        private String hashToken(String token) {

                try {

                        MessageDigest digest = MessageDigest.getInstance("SHA-256");

                        byte[] hash = digest.digest(
                                        token.getBytes(
                                                        StandardCharsets.UTF_8));

                        StringBuilder result = new StringBuilder();

                        for (byte b : hash) {
                                result.append(
                                                String.format(
                                                                "%02x",
                                                                b));
                        }

                        return result.toString();

                } catch (Exception ex) {
                        throw new IllegalStateException(
                                        "Could not hash refresh token",
                                        ex);
                }
        }

        public record AuthResult(
                        String accessToken,
                        long expiresIn,
                        String refreshToken) {
        }

        private record RefreshTokenPair(
                        String rawToken,
                        RefreshToken entity) {
        }
}