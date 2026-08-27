package com.pointwest.bootcamp.eventhubri.networking.dto.response;

import java.time.LocalDateTime;

import com.pointwest.bootcamp.eventhubri.networking.enums.ConnectionStatus;

public record AttendeeConnectionResponseDto(
        Long id,
        Long eventId,
        Long requesterUserId,
        Long recipientUserId,
        ConnectionStatus connectionStatus,
        LocalDateTime requestedAt,
        LocalDateTime respondedAt
) {
}
