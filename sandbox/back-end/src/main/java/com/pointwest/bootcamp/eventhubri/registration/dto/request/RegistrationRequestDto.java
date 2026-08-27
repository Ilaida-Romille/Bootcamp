package com.pointwest.bootcamp.eventhubri.registration.dto.request;

import jakarta.validation.constraints.NotNull;

public record RegistrationRequestDto(
        @NotNull Long eventId,
        @NotNull Long userId
) {
}
