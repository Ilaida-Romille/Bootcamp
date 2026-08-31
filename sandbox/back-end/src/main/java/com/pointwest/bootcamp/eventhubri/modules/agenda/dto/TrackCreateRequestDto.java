package com.pointwest.bootcamp.eventhubri.modules.agenda.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TrackCreateRequestDto(
        @NotBlank @Size(max = 255) String name,
        @Size(max = 500) String description,
        @NotNull Integer displayOrder) {

}
