package com.pointwest.bootcamp.eventhubri.networking.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DirectMessageRequestDto(
        @NotNull Long eventId,
        @NotNull Long recipientUserId,
        @NotBlank @Size(max = 4000) String messageBody
) {
}
