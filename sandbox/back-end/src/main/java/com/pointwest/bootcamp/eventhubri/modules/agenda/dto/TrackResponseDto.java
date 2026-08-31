package com.pointwest.bootcamp.eventhubri.modules.agenda.dto;

import java.util.List;

public record TrackResponseDto(
        Long id,
        String name,
        String description,
        Integer displayOrder,
        List<AgendaResponseDto.SessionSummary> sessions
) {
}