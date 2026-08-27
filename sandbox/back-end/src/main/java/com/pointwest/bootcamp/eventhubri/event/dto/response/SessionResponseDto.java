package com.pointwest.bootcamp.eventhubri.event.dto.response;

import java.time.LocalDateTime;

import com.pointwest.bootcamp.eventhubri.event.enums.SessionType;

public record SessionResponseDto(
        Long id,
        Long eventId,
        Long trackId,
        String trackName,
        String title,
        String roomName,
        LocalDateTime startTime,
        LocalDateTime endTime,
        SessionType sessionType,
        Integer maxSeats
) {
}
