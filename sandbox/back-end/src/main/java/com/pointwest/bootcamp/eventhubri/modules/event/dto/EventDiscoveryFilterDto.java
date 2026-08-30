package com.pointwest.bootcamp.eventhubri.modules.event.dto;

import java.time.LocalDateTime;

import com.pointwest.bootcamp.eventhubri.modules.event.entity.Event;

public record EventDiscoveryFilterDto(
        String keyword,
        Event.EventType eventType,
        LocalDateTime startFrom,
        LocalDateTime startTo,
        String location) {

}
