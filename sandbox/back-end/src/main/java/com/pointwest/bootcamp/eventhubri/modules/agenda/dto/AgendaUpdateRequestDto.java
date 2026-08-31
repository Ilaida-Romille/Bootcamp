package com.pointwest.bootcamp.eventhubri.modules.agenda.dto;

import java.time.LocalDate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AgendaUpdateRequestDto(
        @NotNull LocalDate agendaDate,
        @NotBlank String title,
        String description) {
}