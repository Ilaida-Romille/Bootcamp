package com.pointwest.bootcamp.eventhubri.service;

import com.pointwest.bootcamp.eventhubri.dto.ImportEventDto;
import com.pointwest.bootcamp.eventhubri.exception.AppException;
import com.pointwest.bootcamp.eventhubri.model.*;
import com.pointwest.bootcamp.eventhubri.repository.AttendeeRepository;
import com.pointwest.bootcamp.eventhubri.repository.EventRepository;
import com.pointwest.bootcamp.eventhubri.repository.RegistrationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

@Service
@Transactional
public class OrganizerService {

    private final EventRepository eventRepository;
    private final RegistrationRepository registrationRepository;
    private final AttendeeRepository attendeeRepository;

    public OrganizerService(EventRepository eventRepository, RegistrationRepository registrationRepository, AttendeeRepository attendeeRepository) {
        this.eventRepository = eventRepository;
        this.registrationRepository = registrationRepository;
        this.attendeeRepository = attendeeRepository;
    }

    // --- Event CRUD Operations ---

    public Event createEvent(Event event) {
        // if (event.getEventId() == null || event.getEventId().isBlank()) {
        // event.setEventId("EVT-" + UUID.randomUUID().toString().substring(0, 8));
        // }
        return eventRepository.save(event);
    }

    public Event updateEvent(Long eventId, Event updatedEventDetails) {
        Event existingEvent = getEventById(eventId);

        existingEvent.setTitle(updatedEventDetails.getTitle());
        existingEvent.setDescription(updatedEventDetails.getDescription());
        existingEvent.setStatus(updatedEventDetails.getStatus());
        existingEvent.setStartDateTime(updatedEventDetails.getStartDateTime());
        existingEvent.setEndDateTime(updatedEventDetails.getEndDateTime());
        existingEvent.setRegistrationOpensAt(updatedEventDetails.getRegistrationOpensAt());
        existingEvent.setRegistrationClosesAt(updatedEventDetails.getRegistrationClosesAt());
        existingEvent.setVenue(updatedEventDetails.getVenue());
        existingEvent.setCapacity(updatedEventDetails.getCapacity());
        existingEvent.setIsFoodProvided(updatedEventDetails.getIsFoodProvided());

        return eventRepository.save(existingEvent);
    }

    public void deleteEvent(Long eventId) {
        getEventById(eventId); // Throws exception if not found
        eventRepository.deleteById(eventId);
    }

    public List<Event> getAllEvents() throws AppException {
        try {
            List<Event> events = eventRepository.findAll();
            return events;
        } catch (Exception e) {
            throw new AppException("No events registered.", e);
        }

    }

    public Event getEventById(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found with ID: " + eventId));
    }

    public List<Event> getEventsByOrganizer(String organizerId) {
        return eventRepository.findByOrganizerId(organizerId);
    }

    // --- Agenda & Session Management ---

    public Agenda attachAgendaToEvent(Long eventId, Agenda agenda) {
        Event event = getEventById(eventId);
        if (agenda.getAgendaId() == null) {
            agenda.setAgendaId("AGN-" + UUID.randomUUID().toString().substring(0, 8));
        }
        event.setAgenda(agenda);
        eventRepository.save(event);
        return agenda;
    }

    public void addSessionToEventAgenda(Long eventId, Session session) {
        Event event = getEventById(eventId);
        if (event.getAgenda() == null) {
            Agenda newAgenda = new Agenda();
            newAgenda.setAgendaId("AGN-" + UUID.randomUUID().toString().substring(0, 8));
            newAgenda.setSessions(new ArrayList<>());
            event.setAgenda(newAgenda);
        }

        if (session.getSessionId() == null) {
            session.setSessionId("SES-" + UUID.randomUUID().toString().substring(0, 8));
        }

        event.getAgenda().getSessions().add(session);
        eventRepository.save(event);
    }

    // --- Organizer Insights ---

    public List<Registration> getEventRegistrations(Long eventId) {
        getEventById(eventId); // Verify event existence
        return registrationRepository.findByEventId(eventId);
    }

    public List<Attendee> searchAttendeesByName(String name) {
        if (name == null || name.isBlank()) {
            return List.of();
        }
        return attendeeRepository.findByNameContainingIgnoreCase(name);
    }
    
    public void exportEvents(Writer writer) throws IOException, AppException {
        List<Event> events = getAllEvents();

        writer.write("Title,Description,Status,Start Date Time,End Date Time,"
                + "Registration Opens At,Registration Closes At,Venue,Capacity,Food Provided\n");

        for (Event event : events) {
            writer.write(
                    escapeCsv(event.getTitle()) + "," +
                            escapeCsv(event.getDescription()) + "," +
                            escapeCsv(String.valueOf(event.getStatus())) + "," +
                            escapeCsv(String.valueOf(event.getStartDateTime())) + "," +
                            escapeCsv(String.valueOf(event.getEndDateTime())) + "," +
                            escapeCsv(String.valueOf(event.getRegistrationOpensAt())) + "," +
                            escapeCsv(String.valueOf(event.getRegistrationClosesAt())) + "," +
                            escapeCsv(event.getVenue()) + "," +
                            escapeCsv(String.valueOf(event.getCapacity())) + "," +
                            escapeCsv(String.valueOf(event.getIsFoodProvided())) +
                            "\n");
        }
    }

    private String escapeCsv(String value) {
        if (value == null) {
            return "";
        }

        return "\"" + value.replace("\"", "\"\"") + "\"";
    }

    public List<Long> importEvents(MultipartFile file) throws AppException {

        List<Event> events = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream()))) {

            // Skip header
            reader.readLine();

            String line;

            while ((line = reader.readLine()) != null) {

                String[] values = line.split(",", -1);

                ImportEventDto dto = new ImportEventDto();

                dto.setTitle(values[0]);
                dto.setDescription(values[1]);
                dto.setStatus(values[2]);
                dto.setStartDateTime(values[3]);
                dto.setEndDateTime(values[4]);
                dto.setRegistrationOpensAt(values[5]);
                dto.setRegistrationClosesAt(values[6]);
                dto.setVenue(values[7]);
                dto.setCapacity(values[8]);
                dto.setIsFoodProvided(values[9]);

                Event event = new Event();

                event.setTitle(dto.getTitle());
                event.setDescription(dto.getDescription());
                event.setStatus(EventStatus.valueOf(dto.getStatus().toUpperCase()));

                event.setStartDateTime(parseDate(dto.getStartDateTime()));
                event.setEndDateTime(parseDate(dto.getEndDateTime()));
                event.setRegistrationOpensAt(parseDate(dto.getRegistrationOpensAt()));
                event.setRegistrationClosesAt(parseDate(dto.getRegistrationClosesAt()));

                event.setVenue(dto.getVenue());
                event.setCapacity(Integer.parseInt(dto.getCapacity()));
                event.setIsFoodProvided(
                        Boolean.parseBoolean(dto.getIsFoodProvided()));

                events.add(event);
            }

            List<Event> savedEvents = eventRepository.saveAll(events);

            return savedEvents.stream()
                    .map(Event::getEventId)
                    .toList();

        } catch (IOException | RuntimeException e) {
            e.printStackTrace();
            throw new AppException("Failed to import events: " + e.getMessage(), e);
        }
    }

    private Date parseDate(String value) throws AppException {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

            formatter.setLenient(false);

            return formatter.parse(value);

        } catch (ParseException e) {
            throw new AppException(
                    "Invalid date format: " + value +
                            ". Expected format: yyyy-MM-dd'T'HH:mm:ss",
                    e);
        }
    }
}