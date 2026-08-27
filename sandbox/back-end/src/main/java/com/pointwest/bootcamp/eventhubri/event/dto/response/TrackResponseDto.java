package com.pointwest.bootcamp.eventhubri.event.dto.response;

public record TrackResponseDto(
        Long id,
        Long eventId,
        String trackName,
        String trackColor,
        String description
) {
}
