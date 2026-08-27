package com.pointwest.bootcamp.eventhubri.survey.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record LivePollRequestDto(
        @NotNull Long sessionId,
        @NotBlank String questionText,
        @NotEmpty List<@NotBlank String> options
) {
}
