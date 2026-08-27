package com.pointwest.bootcamp.eventhubri.survey.dto.request;

import jakarta.validation.constraints.NotNull;

public record PollResponseRequestDto(
        @NotNull Long livePollId,
        @NotNull Long pollOptionId,
        @NotNull Long registrationId
) {
}
