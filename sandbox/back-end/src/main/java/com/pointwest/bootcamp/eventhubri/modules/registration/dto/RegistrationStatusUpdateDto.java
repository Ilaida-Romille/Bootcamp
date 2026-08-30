package com.pointwest.bootcamp.eventhubri.modules.registration.dto;

import com.pointwest.bootcamp.eventhubri.modules.registration.entity.RegistrationStatus;
import jakarta.validation.constraints.NotNull;

public record RegistrationStatusUpdateDto(
        @NotNull RegistrationStatus status) {
}
