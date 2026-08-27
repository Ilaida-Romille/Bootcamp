package com.pointwest.bootcamp.eventhubri.event.dto.request;

import java.time.LocalDateTime;

import com.pointwest.bootcamp.eventhubri.event.enums.EventType;
import com.pointwest.bootcamp.eventhubri.event.enums.EventVisibility;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record EventRequestDto(
        @NotNull Long organizerId,
        @NotBlank @Size(max = 255) String title,
        String description,
        @NotNull EventType eventType,
        @NotNull EventVisibility visibility,
        @NotNull @Future LocalDateTime startDate,
        @NotNull @Future LocalDateTime endDate,
        @Size(max = 50) String timezone,
        @Size(max = 255) String venueName,
        @Size(max = 255) String venueAddress,
        @Size(max = 255) String virtualMeetingUrl,
        boolean cateringEnabled,
        boolean networkingEnabled,
        @Positive Integer expectedCapacity,
        @Positive Integer maxCapacity,
        Long clonedFromEventId
) {
}
