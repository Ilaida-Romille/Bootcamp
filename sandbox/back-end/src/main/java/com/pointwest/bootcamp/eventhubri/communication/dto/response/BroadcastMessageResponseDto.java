package com.pointwest.bootcamp.eventhubri.communication.dto.response;

import java.time.LocalDateTime;

import com.pointwest.bootcamp.eventhubri.communication.enums.TargetSegment;

public record BroadcastMessageResponseDto(
        Long id,
        Long eventId,
        Long senderUserId,
        String subject,
        TargetSegment targetSegment,
        LocalDateTime scheduledAt
) {
}
