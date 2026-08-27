package com.pointwest.bootcamp.eventhubri.registration.dto.request;

import jakarta.validation.constraints.NotNull;

public record RegistrationSessionPreferenceRequestDto(
        @NotNull Long registrationId,
        @NotNull Long sessionId
) {
}
