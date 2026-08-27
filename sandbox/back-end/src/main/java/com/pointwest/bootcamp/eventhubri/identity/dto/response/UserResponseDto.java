package com.pointwest.bootcamp.eventhubri.identity.dto.response;

import java.time.LocalDateTime;

// Deliberately excludes passwordHash -- a response DTO is also the place
// that enforces "secrets never leave the service layer".
public record UserResponseDto(
        Long id,
        String email,
        String firstName,
        String lastName,
        String phoneNumber,
        Boolean isActive,
        LocalDateTime lastLoginAt,
        LocalDateTime createdAt
) {
}
