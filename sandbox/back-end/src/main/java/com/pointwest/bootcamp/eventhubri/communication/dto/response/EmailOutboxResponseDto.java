package com.pointwest.bootcamp.eventhubri.communication.dto.response;

import java.time.LocalDateTime;

import com.pointwest.bootcamp.eventhubri.communication.enums.EmailStatus;
import com.pointwest.bootcamp.eventhubri.communication.enums.RelatedEntityType;

// Admin/observability read model only -- outbox rows are produced internally
// by the app, never created directly through a public API, so there is no
// request DTO for this resource.
public record EmailOutboxResponseDto(
        Long id,
        RelatedEntityType relatedEntityType,
        Long relatedEntityId,
        String recipientEmail,
        String subject,
        EmailStatus status,
        Integer retryCount,
        LocalDateTime scheduledFor,
        LocalDateTime sentAt
) {
}
