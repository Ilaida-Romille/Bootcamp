package com.pointwest.bootcamp.eventhubri.survey.dto.response;

public record PollOptionResponseDto(
        Long id,
        String optionText,
        Integer displayOrder
) {
}
