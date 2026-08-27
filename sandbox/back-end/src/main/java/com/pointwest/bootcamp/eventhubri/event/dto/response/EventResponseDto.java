package com.pointwest.bootcamp.eventhubri.event.dto.response;

import java.time.LocalDateTime;

import com.pointwest.bootcamp.eventhubri.event.enums.EventStatus;
import com.pointwest.bootcamp.eventhubri.event.enums.EventType;
import com.pointwest.bootcamp.eventhubri.event.enums.EventVisibility;

public record EventResponseDto(
        Long id,
        Long organizerId,
        String organizerName,
        String title,
        String description,
        EventType eventType,
        EventVisibility visibility,
        LocalDateTime startDate,
        LocalDateTime endDate,
        String timezone,
        String venueName,
        String virtualMeetingUrl,
        boolean cateringEnabled,
        boolean networkingEnabled,
        Integer expectedCapacity,
        Integer maxCapacity,
        EventStatus status
) {
}
