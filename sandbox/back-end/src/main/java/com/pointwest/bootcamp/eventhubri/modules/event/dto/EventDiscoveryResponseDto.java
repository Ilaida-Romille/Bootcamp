package com.pointwest.bootcamp.eventhubri.modules.event.dto;

import java.time.LocalDateTime;

import com.pointwest.bootcamp.eventhubri.modules.event.entity.Event;

public record EventDiscoveryResponseDto(
        Long id,
        Long organizationId,
        String organizationName,
        String title,
        String description,
        String bannerImageUrl,
        Event.EventType eventType,
        String locationAddress,
        String virtualMeetingUrl,
        LocalDateTime startTime,
        LocalDateTime endTime,
        boolean cateringProvided,
        Integer maxCapacity,
        Integer availableSlots,
        boolean registrationOpen) {
}
