package com.pointwest.bootcamp.eventhubri.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.pointwest.bootcamp.eventhubri.auth.dto.AuthResponse;
import com.pointwest.bootcamp.eventhubri.auth.dto.LoginRequest;
import com.pointwest.bootcamp.eventhubri.auth.dto.MessageResponse;
import com.pointwest.bootcamp.eventhubri.auth.dto.RefreshTokenRequest;
import com.pointwest.bootcamp.eventhubri.auth.service.AuthService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request.email(), request.password()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(authService.refresh(request.refreshToken()));
    }

    @PostMapping("/logout")
    public ResponseEntity<MessageResponse> logout(@Valid @RequestBody RefreshTokenRequest request) {
        authService.logout(request.refreshToken());
        return ResponseEntity.ok(new MessageResponse("Refresh token revoked"));
    }
}
