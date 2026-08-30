package com.pointwest.bootcamp.eventhubri.modules.registration.dto;

import jakarta.validation.constraints.NotNull;

public record RegistrationRequestDto(
        @NotNull Long eventId) {
}
