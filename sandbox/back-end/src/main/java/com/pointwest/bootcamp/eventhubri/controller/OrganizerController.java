package com.pointwest.bootcamp.eventhubri.controller;

import com.pointwest.bootcamp.eventhubri.dto.AgendaDto;
import com.pointwest.bootcamp.eventhubri.dto.EventDto;
import com.pointwest.bootcamp.eventhubri.dto.RegistrationDto;
import com.pointwest.bootcamp.eventhubri.model.Agenda;
import com.pointwest.bootcamp.eventhubri.model.Event;
import com.pointwest.bootcamp.eventhubri.model.Registration;
import com.pointwest.bootcamp.eventhubri.model.Session;
import com.pointwest.bootcamp.eventhubri.service.OrganizerService;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class OrganizerController {

    private final OrganizerService organizerService;

    public OrganizerController(OrganizerService organizerService) {
        this.organizerService = organizerService;
    }

    public EventDto createEvent(Event event) {
        System.out.println("Creating Event: " + event.getTitle());
        Event createdEvent = organizerService.createEvent(event);
        return new EventDto(createdEvent);
    }

    public EventDto updateEvent(Long eventId, Event updatedEventDetails) {
        System.out.println("Updating Event: " + eventId);
        Event updatedEvent = organizerService.updateEvent(eventId, updatedEventDetails);
        return new EventDto(updatedEvent);
    }

    public void deleteEvent(Long eventId) {
        System.out.println("Deleting Event: " + eventId);
        organizerService.deleteEvent(eventId);
    }

    public EventDto getEvent(Long eventId) {
        System.out.println("Fetching Event details: " + eventId);
        Event event = organizerService.getEventById(eventId);
        return new EventDto(event);
    }

    public List<EventDto> getOrganizerEvents(String organizerId) {
        System.out.println("Fetching Events for Organizer: " + organizerId);
        return organizerService.getEventsByOrganizer(organizerId).stream()
                .map(EventDto::new)
                .toList();
    }

    public AgendaDto attachAgenda(Long eventId, Agenda agenda) {
        System.out.println("Attaching Agenda to Event: " + eventId);
        Agenda savedAgenda = organizerService.attachAgendaToEvent(eventId, agenda);
        return new AgendaDto(savedAgenda);
    }

    public void addSessionToAgenda(Long eventId, Session session) {
        System.out.println("Adding Session to Event Agenda: " + eventId);
        organizerService.addSessionToEventAgenda(eventId, session);
    }

    public List<RegistrationDto> getEventRegistrations(Long eventId) {
        System.out.println("Fetching Registrations for Event: " + eventId);
        List<Registration> registrations = organizerService.getEventRegistrations(eventId);
        return registrations.stream()
                .map(RegistrationDto::new)
                .toList();
    }
}