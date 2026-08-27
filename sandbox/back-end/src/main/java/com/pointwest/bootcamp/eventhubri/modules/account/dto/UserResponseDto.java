package com.pointwest.bootcamp.eventhubri.modules.account.dto;

import java.time.LocalDateTime;

import com.pointwest.bootcamp.eventhubri.modules.account.entity.AppUser;
import com.pointwest.bootcamp.eventhubri.modules.account.entity.Role;

// passwordHash intentionally has no place here — it must never cross the wire.
public record UserResponseDto(
        Long id,
        String email,
        String firstName,
        String lastName,
        Role role,
        AppUser.Status status,
        Long organizationId,
        LocalDateTime createdAt) {
}
