package com.pointwest.bootcamp.eventhubri.identity.dto.request;

import com.pointwest.bootcamp.eventhubri.identity.enums.RoleType;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RoleRequestDto(
        @NotNull RoleType roleName,
        @Size(max = 255) String description
) {
}
