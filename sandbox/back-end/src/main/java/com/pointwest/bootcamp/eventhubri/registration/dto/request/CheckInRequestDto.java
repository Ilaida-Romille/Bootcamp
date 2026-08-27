package com.pointwest.bootcamp.eventhubri.registration.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CheckInRequestDto(
        @NotNull Long eTicketId,
        @NotBlank String entryPoint
) {
}
