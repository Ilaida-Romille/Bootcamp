package com.pointwest.bootcamp.eventhubri;

import com.pointwest.bootcamp.eventhubri.controller.AgendaController;
import com.pointwest.bootcamp.eventhubri.controller.RegistrationController;
import com.pointwest.bootcamp.eventhubri.dto.AgendaResponseDto;
import com.pointwest.bootcamp.eventhubri.dto.RegisterAttendeeRequestDto;
import com.pointwest.bootcamp.eventhubri.dto.RegistrationResponseDto;
import com.pointwest.bootcamp.eventhubri.dto.SessionResponseDto;
import com.pointwest.bootcamp.eventhubri.model.*;
import com.pointwest.bootcamp.eventhubri.repository.EventRepository;
import com.pointwest.bootcamp.eventhubri.repository.RegistrationRepository;
import com.pointwest.bootcamp.eventhubri.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EventhubriApplicationTests {

    @Autowired
    private RegistrationController registrationController;

    @Autowired
    private AgendaController agendaController;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RegistrationRepository registrationRepository;

    @BeforeEach
    void setUp() {
        // Reset/reseed in-memory repository states before each test run
        eventRepository.findAll().clear();
        userRepository.findAll().clear();
        registrationRepository.findAll().clear();

        // Seed default test user (Attendee)
        Attendee attendee = new Attendee("U101", "Jane Doe", "jane@example.com", "A101");
        userRepository.save(attendee);

        // Seed default test event
        Event event = new Event();
        event.setEventId("E1001");
        event.setTitle("Spring Boot Bootcamp");
        event.setDescription("Core Spring Boot Training");
        event.setStatus(EventStatus.PUBLISHED);
        event.setCapacity(100);
        event.setFoodProvided(true);
        event.setOrganizerId("O201");
        event.setStartDateTime(LocalDateTime.now().plusDays(5));
        event.setEndDateTime(LocalDateTime.now().plusDays(5).plusHours(2));
        eventRepository.save(event);
    }

    @Test
    void contextLoads() {
        assertNotNull(registrationController);
        assertNotNull(agendaController);
    }

    // ==========================================
    // DOMAIN MODEL TESTS
    // ==========================================

    @Test
    @DisplayName("Model: Should initialize Registration domain entity correctly")
    void testRegistrationModelInitialization() {
        Attendee attendee = (Attendee) userRepository.findById("U101").orElseThrow();
        Event event = eventRepository.findById("E1001").orElseThrow();

        Registration registration = new Registration("R101", attendee, event, "Vegan");

        assertEquals("R101", registration.getRegistrationId());
        assertEquals(attendee, registration.getAttendee());
        assertEquals(event, registration.getEvent());
        assertEquals("Vegan", registration.getDietaryRestrictions());
        assertEquals(RegistrationStatus.CONFIRMED, registration.getStatus());
        assertNotNull(registration.getRegisteredAt());
    }

    // ==========================================
    // REGISTRATION CONTROLLER & SERVICE TESTS
    // ==========================================

    @Test
    @DisplayName("Registration: Should successfully register an attendee")
    void testRegisterAttendeeSuccess() {
        RegisterAttendeeRequestDto requestDto = new RegisterAttendeeRequestDto(
                "U101",
                "E1001",
                "Nut Allergy",
                Collections.emptyList()
        );

        RegistrationResponseDto response = registrationController.registerAttendee(requestDto);

        assertNotNull(response);
        assertNotNull(response.getRegistrationId());
        assertEquals("A101", response.getAttendeeId());
        assertEquals("E1001", response.getEventId());
        assertEquals("Nut Allergy", response.getDietaryRestrictions());
        assertEquals(RegistrationStatus.CONFIRMED, response.getStatus());
    }

    @Test
    @DisplayName("Registration: Should throw exception on duplicate registration")
    void testDuplicateRegistrationThrowsException() {
        RegisterAttendeeRequestDto requestDto = new RegisterAttendeeRequestDto(
                "U101",
                "E1001",
                "None",
                Collections.emptyList()
        );

        // First registration succeeds
        registrationController.registerAttendee(requestDto);

        // Second registration must fail
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> registrationController.registerAttendee(requestDto)
        );

        assertEquals("Attendee is already registered for this event.", exception.getMessage());
    }

    @Test
    @DisplayName("Registration: Should enforce event capacity limit")
    void testEventCapacityExceededThrowsException() {
        // Limit capacity to 1
        Event event = eventRepository.findById("E1001").orElseThrow();
        event.setCapacity(1);
        eventRepository.save(event);

        // Register First Attendee (Succeeds)
        RegisterAttendeeRequestDto dto1 = new RegisterAttendeeRequestDto("U101", "E1001", "None", Collections.emptyList());
        registrationController.registerAttendee(dto1);

        // Add and attempt registering Second Attendee (Fails)
        Attendee secondAttendee = new Attendee("U102", "Bob Smith", "bob@example.com", "A102");
        userRepository.save(secondAttendee);

        RegisterAttendeeRequestDto dto2 = new RegisterAttendeeRequestDto("U102", "E1001", "None", Collections.emptyList());

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> registrationController.registerAttendee(dto2)
        );

        assertEquals("Event capacity reached. Cannot complete registration.", exception.getMessage());
    }

    @Test
    @DisplayName("Registration: Should cancel registration successfully")
    void testCancelRegistration() {
        RegisterAttendeeRequestDto requestDto = new RegisterAttendeeRequestDto("U101", "E1001", "None", Collections.emptyList());
        RegistrationResponseDto response = registrationController.registerAttendee(requestDto);

        registrationController.cancelRegistration(response.getRegistrationId());

        RegistrationResponseDto updated = registrationController.getRegistrationById(response.getRegistrationId());
        assertEquals(RegistrationStatus.CANCELLED, updated.getStatus());
    }

    @Test
    @DisplayName("Registration: Should query list of registrations by event ID")
    void testGetRegistrationsByEventId() {
        RegisterAttendeeRequestDto requestDto = new RegisterAttendeeRequestDto("U101", "E1001", "None", Collections.emptyList());
        registrationController.registerAttendee(requestDto);

        List<RegistrationResponseDto> registrations = registrationController.getRegistrationsByEventId("E1001");

        assertNotNull(registrations);
        assertEquals(1, registrations.size());
        assertEquals("E1001", registrations.get(0).getEventId());
    }

    // ==========================================
    // AGENDA CONTROLLER & SERVICE TESTS
    // ==========================================

    @Test
    @DisplayName("Agenda: Should add a presentation session to event and query agenda")
    void testAddSessionAndGetAgenda() {
        PresentationSession session = new PresentationSession(
                "S101",
                "Keynote Address",
                "Opening keynote on Java architecture",
                LocalDateTime.now().plusDays(5),
                LocalDateTime.now().plusDays(5).plusHours(1),
                "Hall A",
                "Dr. James Gosling"
        );

        SessionResponseDto sessionResponse = agendaController.addSessionToEvent("E1001", session);

        assertNotNull(sessionResponse);
        assertEquals("S101", sessionResponse.getSessionId());
        assertEquals("PRESENTATION", sessionResponse.getSessionType());
        assertEquals("Dr. James Gosling", sessionResponse.getSpeaker());

        AgendaResponseDto agendaResponse = agendaController.getAgendaByEventId("E1001");
        assertNotNull(agendaResponse);
        assertEquals(1, agendaResponse.getSessions().size());
        assertEquals("Keynote Address", agendaResponse.getSessions().get(0).getTitle());
    }
}