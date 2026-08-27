package com.pointwest.bootcamp.eventhubri.modules.agenda.dto;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SessionCreateRequestDto(

        @NotNull Long trackId,

        @NotBlank @Size(max = 255) String title,

        String description,

        @NotNull LocalDateTime startTime,

        @NotNull LocalDateTime endTime,

        String locationOrRoom,

        List<Long> speakerIds) {
}
