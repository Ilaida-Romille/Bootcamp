package com.pointwest.bootcamp.eventhubri.modules.registration.dto;

import com.pointwest.bootcamp.eventhubri.modules.registration.entity.RegistrationStatus;

import java.time.LocalDateTime;

public record RegistrationResponseDto(
        Long id,
        Long eventId,
        String eventTitle,
        LocalDateTime eventStartTime,
        LocalDateTime eventEndTime,
        Long attendeeId,
        String attendeeName,
        RegistrationStatus status,
        LocalDateTime checkedInAt,
        LocalDateTime registeredAt) {
}
