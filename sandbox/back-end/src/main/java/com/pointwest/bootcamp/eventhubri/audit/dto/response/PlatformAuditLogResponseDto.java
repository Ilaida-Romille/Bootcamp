package com.pointwest.bootcamp.eventhubri.audit.dto.response;

import java.time.LocalDateTime;

// No request DTO: audit rows are written internally by the app on every
// significant action, never accepted as client input -- allowing a client to
// author audit-log entries would defeat the purpose of an audit trail.
public record PlatformAuditLogResponseDto(
        Long id,
        Long actorUserId,
        String actionType,
        String entityType,
        Long entityId,
        String details,
        String ipAddress,
        LocalDateTime createdAt
) {
}
