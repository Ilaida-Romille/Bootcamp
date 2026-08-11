package com.pointwest.bootcamp.eventhubri.service;

import com.pointwest.bootcamp.eventhubri.dto.CreateEventRequest;
import com.pointwest.bootcamp.eventhubri.dto.EventResponse;

import java.util.List;

public interface EventService {
    EventResponse createEvent(CreateEventRequest request);
    EventResponse getEventByEventId(String eventId);
    List<EventResponse> getAllEvents();
    EventResponse updateEvent(String eventId, CreateEventRequest request);
    void deleteEvent(String eventId);
}