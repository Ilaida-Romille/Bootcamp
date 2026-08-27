package com.pointwest.bootcamp.eventhubri.communication.dto.request;

import java.time.LocalDateTime;

import com.pointwest.bootcamp.eventhubri.communication.enums.TargetSegment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record BroadcastMessageRequestDto(
        @NotNull Long eventId,
        @NotBlank @Size(max = 255) String subject,
        @NotBlank String body,
        @NotNull TargetSegment targetSegment,
        LocalDateTime scheduledAt
) {
}
