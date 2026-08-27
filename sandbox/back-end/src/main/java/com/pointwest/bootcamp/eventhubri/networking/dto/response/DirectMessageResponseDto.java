package com.pointwest.bootcamp.eventhubri.networking.dto.response;

import java.time.LocalDateTime;

public record DirectMessageResponseDto(
        Long id,
        Long eventId,
        Long senderUserId,
        Long recipientUserId,
        String messageBody,
        LocalDateTime sentAt,
        LocalDateTime readAt
) {
}
