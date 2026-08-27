package com.pointwest.bootcamp.eventhubri.modules.agenda.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record AgendaResponseDto(
        Long id,
        Long eventId,
        LocalDate agendaDate,
        String title,
        String description,
        List<TrackSummary> tracks) {

    public record TrackSummary(
            Long id,
            String name,
            String description,
            Integer displayOrder,
            List<SessionSummary> sessions) {
    }

    public record SessionSummary(
            Long id,
            String title,
            LocalDateTime startTime,
            LocalDateTime endTime,
            String locationOrRoom) {
    }
}
