package com.pointwest.bootcamp.eventhubri.survey.dto.response;

import java.time.LocalDateTime;

// Named "SurveyAnswer..." (not "SurveyResponseResponseDto") to keep intent
// readable -- this represents one submitted answer to one survey question.
public record SurveyAnswerResponseDto(
        Long id,
        Long surveyQuestionId,
        Long registrationId,
        String responseValue,
        LocalDateTime submittedAt
) {
}
