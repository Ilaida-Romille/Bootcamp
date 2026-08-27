package com.pointwest.bootcamp.eventhubri.survey.dto.response;

import java.time.LocalDateTime;

public record PollResponseResponseDto(
        Long id,
        Long livePollId,
        Long pollOptionId,
        Long registrationId,
        LocalDateTime respondedAt
) {
}
