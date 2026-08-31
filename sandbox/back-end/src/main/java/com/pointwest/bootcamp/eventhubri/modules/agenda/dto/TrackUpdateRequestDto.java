package com.pointwest.bootcamp.eventhubri.modules.agenda.dto;

import jakarta.validation.constraints.NotBlank;

public record TrackUpdateRequestDto(
        @NotBlank String name,
        String description,
        Integer displayOrder) {
}