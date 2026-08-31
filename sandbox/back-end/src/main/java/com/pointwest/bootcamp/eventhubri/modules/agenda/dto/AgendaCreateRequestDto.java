package com.pointwest.bootcamp.eventhubri.modules.agenda.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AgendaCreateRequestDto(
        @NotNull LocalDate agendaDate,
        @NotBlank @Size(max = 255) String title,
        @Size(max = 1000) String description) {

}
