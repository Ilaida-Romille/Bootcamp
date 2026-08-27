package com.pointwest.bootcamp.eventhubri.survey.dto.response;

import com.pointwest.bootcamp.eventhubri.survey.enums.QuestionType;

public record SurveyQuestionResponseDto(
        Long id,
        Long surveyId,
        String questionText,
        QuestionType questionType,
        Integer displayOrder
) {
}
