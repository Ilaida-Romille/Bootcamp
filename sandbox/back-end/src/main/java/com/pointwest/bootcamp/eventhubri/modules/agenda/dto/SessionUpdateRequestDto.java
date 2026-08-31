package com.pointwest.bootcamp.eventhubri.modules.agenda.dto;

import java.time.LocalDateTime;
import java.util.List;
import jakarta.validation.constraints.NotBlank;

public record SessionUpdateRequestDto(
        Long trackId,
        @NotBlank String title,
        String description,
        LocalDateTime startTime,
        LocalDateTime endTime,
        String locationOrRoom,
        List<Long> speakerIds) {
}