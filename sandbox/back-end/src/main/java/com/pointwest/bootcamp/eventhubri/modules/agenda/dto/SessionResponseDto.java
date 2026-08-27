package com.pointwest.bootcamp.eventhubri.modules.agenda.dto;

import java.time.LocalDateTime;
import java.util.List;

public record SessionResponseDto(
        Long id,
        Long trackId,
        String title,
        String description,
        LocalDateTime startTime,
        LocalDateTime endTime,
        String locationOrRoom,
        List<SpeakerSummary> speakers) {

    public record SpeakerSummary(
            Long id,
            String fullName,
            String organizationOrTitle,
            String photoUrl) {
    }
}
