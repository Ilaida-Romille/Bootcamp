package com.pointwest.bootcamp.eventhubri.event.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TrackRequestDto(
        @NotNull Long eventId,
        @NotBlank @Size(max = 255) String trackName,
        @Size(max = 20) String trackColor,
        @Size(max = 255) String description
) {
}
