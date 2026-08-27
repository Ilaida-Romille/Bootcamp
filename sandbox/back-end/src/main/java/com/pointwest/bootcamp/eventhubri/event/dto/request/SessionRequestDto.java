package com.pointwest.bootcamp.eventhubri.event.dto.request;

import java.time.LocalDateTime;

import com.pointwest.bootcamp.eventhubri.event.enums.SessionType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record SessionRequestDto(
        @NotNull Long eventId,
        Long trackId,
        @NotBlank String title,
        String description,
        String roomName,
        @NotNull LocalDateTime startTime,
        @NotNull LocalDateTime endTime,
        @NotNull SessionType sessionType,
        @Positive Integer maxSeats
) {
}
