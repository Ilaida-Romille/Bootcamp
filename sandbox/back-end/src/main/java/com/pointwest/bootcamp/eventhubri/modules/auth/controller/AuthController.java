package com.pointwest.bootcamp.eventhubri.modules.auth.controller;

import com.pointwest.bootcamp.eventhubri.modules.account.dto.UserResponseDto;
import com.pointwest.bootcamp.eventhubri.modules.account.entity.AppUser;
import com.pointwest.bootcamp.eventhubri.modules.auth.dto.*;
import com.pointwest.bootcamp.eventhubri.modules.auth.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private static final String REFRESH_COOKIE = "eventhub_refresh";

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Void> register(
            @Valid @RequestBody RegisterOrganizerRequestDto request) {

        authService.registerOrganizer(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @PostMapping("/register/attendee")
    public ResponseEntity<Void> registerAttendee(
            @Valid @RequestBody RegisterAttendeeRequestDto request) {

        authService.registerAttendee(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @GetMapping("/organizations")
    public ResponseEntity<List<PublicOrganizationDto>> getPublicOrganizations() {
        return ResponseEntity.ok(authService.getPublicOrganizations());
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(
            @Valid @RequestBody LoginRequestDto request,
            HttpServletResponse response) {

        AuthService.AuthResult result = authService.login(request);

        addRefreshCookie(
                response,
                result.refreshToken());

        return ResponseEntity.ok(
                new AuthResponseDto(
                        result.accessToken(),
                        "Bearer",
                        result.expiresIn(),
                        null));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDto> refresh(
            @CookieValue(name = REFRESH_COOKIE, required = false) String refreshToken,
            HttpServletResponse response) {

        if (refreshToken == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .build();
        }

        AuthService.AuthResult result = authService.refresh(refreshToken);

        addRefreshCookie(
                response,
                result.refreshToken());

        return ResponseEntity.ok(
                new AuthResponseDto(
                        result.accessToken(),
                        "Bearer",
                        result.expiresIn(),
                        null));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @CookieValue(name = REFRESH_COOKIE, required = false) String refreshToken,
            HttpServletResponse response) {

        authService.logout(refreshToken);

        clearRefreshCookie(response);

        return ResponseEntity.noContent().build();
    }

    private void addRefreshCookie(
            HttpServletResponse response,
            String token) {

        ResponseCookie cookie = ResponseCookie
                .from(
                        REFRESH_COOKIE,
                        token)
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/api/auth")
                .maxAge(
                        java.time.Duration
                                .ofDays(30))
                .build();

        response.addHeader(
                HttpHeaders.SET_COOKIE,
                cookie.toString());
    }

    private void clearRefreshCookie(
            HttpServletResponse response) {

        ResponseCookie cookie = ResponseCookie
                .from(
                        REFRESH_COOKIE,
                        "")
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/api/auth")
                .maxAge(0)
                .build();

        response.addHeader(
                HttpHeaders.SET_COOKIE,
                cookie.toString());
    }
}