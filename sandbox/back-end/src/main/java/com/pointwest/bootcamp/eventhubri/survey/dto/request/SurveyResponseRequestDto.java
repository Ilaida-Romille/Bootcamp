package com.pointwest.bootcamp.eventhubri.survey.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SurveyResponseRequestDto(
        @NotNull Long surveyQuestionId,
        @NotNull Long registrationId,
        @NotBlank String responseValue
) {
}
