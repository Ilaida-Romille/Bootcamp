package com.pointwest.bootcamp.eventhubri.modules.event.service;

import com.pointwest.bootcamp.eventhubri.modules.event.dto.EventDiscoveryFilterDto;
import com.pointwest.bootcamp.eventhubri.modules.event.dto.EventDiscoveryResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EventAttendeeService {

    Page<EventDiscoveryResponseDto> browsedPublishedEvents(EventDiscoveryFilterDto filter, Pageable pageable);

    EventDiscoveryResponseDto getPublishedEvents(Long eventId);
}