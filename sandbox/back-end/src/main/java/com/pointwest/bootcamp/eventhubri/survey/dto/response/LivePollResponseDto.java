package com.pointwest.bootcamp.eventhubri.survey.dto.response;

import java.util.List;

public record LivePollResponseDto(
        Long id,
        Long sessionId,
        String questionText,
        boolean isOpen,
        List<PollOptionResponseDto> options
) {
}
