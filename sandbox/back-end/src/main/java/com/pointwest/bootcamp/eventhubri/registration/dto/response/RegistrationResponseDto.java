package com.pointwest.bootcamp.eventhubri.registration.dto.response;

import java.time.LocalDateTime;

import com.pointwest.bootcamp.eventhubri.registration.enums.RegistrationStatus;

public record RegistrationResponseDto(
        Long id,
        Long eventId,
        String eventTitle,
        Long userId,
        RegistrationStatus registrationStatus,
        LocalDateTime registeredAt,
        LocalDateTime cancelledAt
) {
}
