package com.pointwest.bootcamp.eventhubri.modules.employee.dto;

public record RegisteredEventSummaryDto(Long registrationId, Long eventId, String title,
        boolean canCancelRegistration) {
}
