package com.pointwest.bootcamp.eventhubri.auth.exception;

import java.time.Instant;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AuthExceptionHandler {

    @ExceptionHandler({InvalidRefreshTokenException.class, RefreshTokenReuseException.class})
    ResponseEntity<Map<String, Object>> handleRefreshTokenException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorBody(401, "Unauthorized", ex.getMessage()));
    }

    @ExceptionHandler({BadCredentialsException.class, DisabledException.class})
    ResponseEntity<Map<String, Object>> handleLoginAuthenticationException(AuthenticationException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
            errorBody(401, "Unauthorized", "Invalid email or password"));
    }

    private Map<String, Object> errorBody(int status, String error, String message) {
        return Map.of(
            "timestamp", Instant.now().toString(),
            "status", status,
            "error", error,
            "message", message
        );
    }
}
