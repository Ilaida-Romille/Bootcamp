package com.pointwest.bootcamp.eventhubri.modules.communication.dto;

import java.time.LocalDateTime;

import com.pointwest.bootcamp.eventhubri.modules.communication.entity.DeliveryStatus;

public record NotificationLogResponseDto(
        Long id,
        Long eventId,
        Long senderUserId,
        String senderName,
        // Null when this row represents a BroadcastNoticeLog.
        Long recipientUserId,
        String notificationType,
        String subject,
        String messageBody,
        LocalDateTime sentAt,
        DeliveryStatus deliveryStatus) {
}
