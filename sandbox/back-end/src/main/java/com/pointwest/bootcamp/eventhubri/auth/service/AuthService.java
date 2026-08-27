package com.pointwest.bootcamp.eventhubri.auth.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.pointwest.bootcamp.eventhubri.auth.dto.AuthResponse;
import com.pointwest.bootcamp.eventhubri.identity.entity.User;
import com.pointwest.bootcamp.eventhubri.identity.repository.UserRepository;
import com.pointwest.bootcamp.eventhubri.security.jwt.JwtTokenProvider;
import com.pointwest.bootcamp.eventhubri.security.service.UserAuthorityProvider;
import com.pointwest.bootcamp.eventhubri.security.service.UserPrincipal;

@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RefreshTokenService refreshTokenService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserAuthorityProvider authorityProvider;

    public AuthService(
            AuthenticationManager authenticationManager,
            UserRepository userRepository,
            RefreshTokenService refreshTokenService,
            JwtTokenProvider jwtTokenProvider,
            UserAuthorityProvider authorityProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.refreshTokenService = refreshTokenService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authorityProvider = authorityProvider;
    }

    @Transactional
    public AuthResponse login(String email, String password) {
        Authentication authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken.unauthenticated(email.trim(), password));

        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        User user = principal.getUser();

        var authorities = authorityProvider.getAuthorities(user);
        SecurityContextHolder.getContext().setAuthentication(
            new UsernamePasswordAuthenticationToken(user, null, authorities));

        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);

        List<String> roles = authorities.stream()
            .map(a -> a.getAuthority())
            .toList();

        String accessToken = jwtTokenProvider.generateAccessToken(
            user.getId(), user.getEmail(), roles);
        RefreshTokenService.IssuedRefreshToken refresh =
            refreshTokenService.issue(user);

        return new AuthResponse(
            "Bearer",
            accessToken,
            jwtTokenProvider.getAccessTokenTtlSeconds(),
            refresh.rawToken());
    }

    @Transactional
    public AuthResponse refresh(String refreshToken) {
        RefreshTokenService.RotatedRefreshToken rotated =
            refreshTokenService.rotate(refreshToken);

        List<String> roles = authorityProvider.getAuthorities(rotated.user()).stream()
            .map(a -> a.getAuthority())
            .toList();

        String accessToken = jwtTokenProvider.generateAccessToken(
            rotated.user().getId(), rotated.user().getEmail(), roles);

        return new AuthResponse(
            "Bearer",
            accessToken,
            jwtTokenProvider.getAccessTokenTtlSeconds(),
            rotated.rawToken());
    }

    @Transactional
    public void logout(String refreshToken) {
        refreshTokenService.revoke(refreshToken);
    }
}
