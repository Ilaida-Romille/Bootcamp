package com.pointwest.bootcamp.eventhubri.survey.dto.request;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SurveyRequestDto(
        @NotNull Long eventId,
        @NotBlank String title,
        LocalDateTime opensAt,
        LocalDateTime closesAt
) {
}
