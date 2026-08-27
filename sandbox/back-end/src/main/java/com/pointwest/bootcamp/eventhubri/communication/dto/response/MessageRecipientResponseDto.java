package com.pointwest.bootcamp.eventhubri.communication.dto.response;

import java.time.LocalDateTime;

import com.pointwest.bootcamp.eventhubri.communication.enums.DeliveryStatus;

// No request DTO -- recipients are fan-out by the service layer from a
// broadcast + target segment, never submitted individually by a client.
public record MessageRecipientResponseDto(
        Long id,
        Long broadcastMessageId,
        Long registrationId,
        DeliveryStatus deliveryStatus,
        LocalDateTime deliveredAt
) {
}
