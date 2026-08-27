package com.pointwest.bootcamp.eventhubri.registration.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegistrationFieldAnswerRequestDto(
        @NotNull Long registrationId,
        @NotNull Long eventCustomFieldId,
        @NotBlank String answerValue
) {
}
