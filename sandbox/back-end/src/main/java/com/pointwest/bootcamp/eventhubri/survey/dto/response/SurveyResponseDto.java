package com.pointwest.bootcamp.eventhubri.survey.dto.response;

import java.time.LocalDateTime;

public record SurveyResponseDto(
        Long id,
        Long eventId,
        String title,
        boolean isActive,
        LocalDateTime opensAt,
        LocalDateTime closesAt
) {
}
