package com.pointwest.bootcamp.eventhubri.modules.profile.dto;

import jakarta.validation.constraints.Size;

public record UserProfileUpdateDto(
        @Size(max = 100) String firstName,
        @Size(max = 100) String lastName,
        @Size(max = 255) String company,
        @Size(max = 255) String dietary) {
}
