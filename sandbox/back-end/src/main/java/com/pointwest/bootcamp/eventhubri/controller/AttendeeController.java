package com.pointwest.bootcamp.eventhubri.controller;

import com.pointwest.bootcamp.eventhubri.dto.AgendaDto;
import com.pointwest.bootcamp.eventhubri.dto.EventDto;
import com.pointwest.bootcamp.eventhubri.dto.RegistrationDto;
import com.pointwest.bootcamp.eventhubri.dto.SessionDto;
import com.pointwest.bootcamp.eventhubri.model.Agenda;
import com.pointwest.bootcamp.eventhubri.model.Event;
import com.pointwest.bootcamp.eventhubri.model.Registration;
import com.pointwest.bootcamp.eventhubri.model.Session;
import com.pointwest.bootcamp.eventhubri.service.AttendeeService;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class AttendeeController {

    private final AttendeeService attendeeService;

    public AttendeeController(AttendeeService attendeeService) {
        this.attendeeService = attendeeService;
    }

    public List<EventDto> browseAllEvents() {
        System.out.println("Browsing all available events");
        return attendeeService.getAllAvailableEvents().stream()
                .map(EventDto::new)
                .toList();
    }

    public EventDto viewEventDetails(String eventId) {
        System.out.println("Viewing details for Event: " + eventId);
        Event event = attendeeService.getEventDetails(eventId);
        return new EventDto(event);
    }

    public AgendaDto viewEventAgenda(String eventId) {
        System.out.println("Viewing agenda for Event: " + eventId);
        Agenda agenda = attendeeService.getEventAgenda(eventId);
        return agenda != null ? new AgendaDto(agenda) : null;
    }

    public List<SessionDto> viewEventSessions(String eventId) {
        System.out.println("Viewing sessions for Event: " + eventId);
        List<Session> sessions = attendeeService.getEventSessions(eventId);
        return sessions.stream()
                .map(SessionDto::new)
                .toList();
    }

    public List<RegistrationDto> viewMyRegistrations(String attendeeId) {
        System.out.println("Viewing registrations for Attendee: " + attendeeId);
        List<Registration> registrations = attendeeService.getMyRegistrations(attendeeId);
        return registrations.stream()
                .map(RegistrationDto::new)
                .toList();
    }
}