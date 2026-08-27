package com.pointwest.bootcamp.eventhubri.survey.dto.request;

import com.pointwest.bootcamp.eventhubri.survey.enums.QuestionType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SurveyQuestionRequestDto(
        @NotNull Long surveyId,
        @NotBlank String questionText,
        @NotNull QuestionType questionType,
        Integer displayOrder
) {
}
