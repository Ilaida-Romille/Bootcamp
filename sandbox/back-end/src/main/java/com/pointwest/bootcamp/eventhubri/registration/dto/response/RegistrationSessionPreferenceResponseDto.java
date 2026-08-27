package com.pointwest.bootcamp.eventhubri.registration.dto.response;

import java.time.LocalDateTime;

public record RegistrationSessionPreferenceResponseDto(
        Long id,
        Long registrationId,
        Long sessionId,
        String sessionTitle,
        LocalDateTime selectedAt
) {
}
