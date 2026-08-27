package com.pointwest.bootcamp.eventhubri.modules.event.dto;

import java.time.LocalDateTime;

import com.pointwest.bootcamp.eventhubri.modules.event.entity.Event;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

// All fields optional — a null field means "leave unchanged" for a partial update.
public record EventUpdateRequestDto(

        @Size(max = 255) String title,

        String description,

        String bannerImageUrl,

        Event.EventType eventType,

        String locationAddress,

        String virtualMeetingUrl,

        LocalDateTime startTime,

        LocalDateTime endTime,

        Boolean isPrivate,

        Boolean cateringProvided,

        @Min(1) Integer maxCapacity,

        Event.Status status) {
}
