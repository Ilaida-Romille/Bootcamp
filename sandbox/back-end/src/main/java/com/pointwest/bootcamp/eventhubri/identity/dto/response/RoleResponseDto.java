package com.pointwest.bootcamp.eventhubri.identity.dto.response;

import com.pointwest.bootcamp.eventhubri.identity.enums.RoleType;

public record RoleResponseDto(
        Long id,
        RoleType roleName,
        String description
) {
}
