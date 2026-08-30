package com.pointwest.bootcamp.eventhubri.modules.event.controller;

import com.pointwest.bootcamp.eventhubri.modules.event.dto.EventDiscoveryFilterDto;
import com.pointwest.bootcamp.eventhubri.modules.event.dto.EventDiscoveryResponseDto;
import com.pointwest.bootcamp.eventhubri.modules.event.entity.Event;
import com.pointwest.bootcamp.eventhubri.modules.event.service.EventService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/events/discover")
@RequiredArgsConstructor
public class EventDiscoveryController {

    private final EventService eventDiscoveryService;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<EventDiscoveryResponseDto>> browse(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Event.EventType eventType,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTo,
            @RequestParam(required = false) String location,
            @PageableDefault(size = 20, sort = "startTime") Pageable pageable) {
        var filter = new EventDiscoveryFilterDto(keyword, eventType, startFrom, startTo, location);
        return ResponseEntity.ok(eventDiscoveryService.browsedPublishedEvents(filter, pageable));
    }

    @GetMapping("/{eventId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<EventDiscoveryResponseDto> getOne(@PathVariable Long eventId) {
        return ResponseEntity.ok(eventDiscoveryService.getPublishedEvents(eventId));
    }
}
