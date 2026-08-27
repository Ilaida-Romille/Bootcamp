package com.pointwest.bootcamp.eventhubri.registration.dto.response;

import java.time.LocalDateTime;

import com.pointwest.bootcamp.eventhubri.registration.enums.QrValidationResult;

public record CheckInResponseDto(
        Long id,
        Long registrationId,
        Long eTicketId,
        Long checkedInByUserId,
        String entryPoint,
        QrValidationResult qrValidationResult,
        LocalDateTime checkedInAt
) {
}
