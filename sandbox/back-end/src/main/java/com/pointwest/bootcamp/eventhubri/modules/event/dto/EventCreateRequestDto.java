package com.pointwest.bootcamp.eventhubri.modules.event.dto;

import java.time.LocalDateTime;

import com.pointwest.bootcamp.eventhubri.modules.event.entity.Event;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record EventCreateRequestDto(

                // organizationId is intentionally absent — resolved server-side from the
                // authenticated organizer's account, never trusted from the client.

                @NotBlank @Size(max = 255) String title,

                String description,

                String bannerImageUrl,

                @NotNull Event.EventType eventType,

                // Required when eventType is PHYSICAL or HYBRID.
                String locationAddress,

                // Required when eventType is VIRTUAL or HYBRID.
                String virtualMeetingUrl,

                @NotNull @Future LocalDateTime startTime,

                @NotNull @Future LocalDateTime endTime,

                @NotNull LocalDateTime registrationOpensAt,

                @NotNull LocalDateTime registrationClosesAt,

                boolean isPrivate,

                boolean cateringProvided,

                @Min(1) Integer maxCapacity) {
}
