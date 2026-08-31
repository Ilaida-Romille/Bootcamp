package com.pointwest.bootcamp.eventhubri.modules.event.dto;

import java.time.LocalDateTime;

import com.pointwest.bootcamp.eventhubri.modules.event.entity.Event;

public record EventResponseDto(
                Long id,
                Long organizationId,
                String organizationName,
                Long createdByUserId,
                String createdByName,
                String title,
                String description,
                String bannerImageUrl,
                Event.EventType eventType,
                String locationAddress,
                String virtualMeetingUrl,
                LocalDateTime startTime,
                LocalDateTime endTime,
                LocalDateTime registrationStartTime,
                LocalDateTime registrationEndTime,
                boolean isPrivate,
                boolean cateringProvided,
                Integer maxCapacity,
                Event.Status status,
                LocalDateTime createdAt,
                LocalDateTime updatedAt) {
}
