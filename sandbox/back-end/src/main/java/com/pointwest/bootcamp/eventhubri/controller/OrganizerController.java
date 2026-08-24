package com.pointwest.bootcamp.eventhubri.controller;

import com.pointwest.bootcamp.eventhubri.dto.AgendaDto;
import com.pointwest.bootcamp.eventhubri.dto.EventDto;
import com.pointwest.bootcamp.eventhubri.dto.RegistrationDto;
import com.pointwest.bootcamp.eventhubri.dto.AttendeeDto;
import com.pointwest.bootcamp.eventhubri.exception.AppException;
import com.pointwest.bootcamp.eventhubri.model.Agenda;
import com.pointwest.bootcamp.eventhubri.model.Event;
import com.pointwest.bootcamp.eventhubri.model.Registration;
import com.pointwest.bootcamp.eventhubri.model.Session;
import com.pointwest.bootcamp.eventhubri.model.Attendee;
import com.pointwest.bootcamp.eventhubri.service.OrganizerService;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/organizer")
public class OrganizerController {

    private final OrganizerService organizerService;

    public OrganizerController(OrganizerService organizerService) {
        this.organizerService = organizerService;
    }

    @GetMapping("/event")
    public List<Event> getAllEvents() throws AppException {
        return organizerService.getAllEvents();
    }

    @PostMapping("/event")
    @ResponseStatus(code = HttpStatus.CREATED)
    public EventDto createEvent(@RequestBody Event event) {
        Event createdEvent = organizerService.createEvent(event);
        return new EventDto(createdEvent);
    }

    @PutMapping("/event/{eventId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void updateEvent(
            @PathVariable Long eventId,
            @RequestBody Event updatedEventDetails) {

        organizerService.updateEvent(eventId, updatedEventDetails);
    }

    @DeleteMapping("/event/{eventId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteEvent(@PathVariable Long eventId) {
        organizerService.deleteEvent(eventId);
    }

    @GetMapping("/event/{eventId}")
    public EventDto getEvent(@PathVariable Long eventId) {
        Event event = organizerService.getEventById(eventId);
        return new EventDto(event);
    }

    @GetMapping("/{organizerId}/events")
    public List<EventDto> getOrganizerEvents(@PathVariable String organizerId) {
        return organizerService.getEventsByOrganizer(organizerId)
                .stream()
                .map(EventDto::new)
                .toList();
    }

    @PostMapping("/event/{eventId}/agenda")
    @ResponseStatus(code = HttpStatus.CREATED)
    public AgendaDto attachAgenda(
            @PathVariable Long eventId,
            @RequestBody Agenda agenda) {

        Agenda savedAgenda = organizerService.attachAgendaToEvent(eventId, agenda);
        return new AgendaDto(savedAgenda);
    }

    @PostMapping("/event/{eventId}/agenda/session")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void addSessionToAgenda(
            @PathVariable Long eventId,
            @RequestBody Session session) {

        organizerService.addSessionToEventAgenda(eventId, session);
    }

    @GetMapping("/event/{eventId}/registrations")
    public List<RegistrationDto> getEventRegistrations(@PathVariable Long eventId) {
        List<Registration> registrations = organizerService.getEventRegistrations(eventId);

        return registrations.stream()
                .map(RegistrationDto::new)
                .toList();
    }

    public List<AttendeeDto> searchAttendeesByName(String name){
        System.out.println("Fetching Attendee by Name: " + name);
        List<Attendee> attendee = organizerService.searchAttendeesByName(name);
        return attendee.stream().map(AttendeeDto::new).toList();  
    }
     
    @GetMapping("/export")
    public void exportEvents(HttpServletResponse response) throws AppException, IOException {
        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"events.csv\"");
        organizerService.exportEvents(response.getWriter());
    }

    @PostMapping("/import")
    @ResponseStatus(code = HttpStatus.CREATED)
    public List<Long> importEvents(@RequestParam MultipartFile file) throws AppException {
        return organizerService.importEvents(file);
    }
}