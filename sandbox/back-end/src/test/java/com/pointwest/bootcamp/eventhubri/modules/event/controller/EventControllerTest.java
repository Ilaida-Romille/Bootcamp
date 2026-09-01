package com.pointwest.bootcamp.eventhubri.modules.event.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pointwest.bootcamp.eventhubri.modules.event.dto.EventCreateRequestDto;
import com.pointwest.bootcamp.eventhubri.modules.event.dto.EventResponseDto;
import com.pointwest.bootcamp.eventhubri.modules.event.dto.EventUpdateRequestDto;
import com.pointwest.bootcamp.eventhubri.modules.event.entity.Event;
import com.pointwest.bootcamp.eventhubri.modules.event.service.EventOrganizerService;

@ExtendWith(MockitoExtension.class)
class EventControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private EventOrganizerService eventOrganizerService;

    @InjectMocks
    private EventController controller;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        objectMapper.findAndRegisterModules();
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    void createEvent_returnsCreatedEvent() throws Exception {
        LocalDateTime start = LocalDateTime.of(2026, 10, 10, 9, 0);
        LocalDateTime end = LocalDateTime.of(2026, 10, 10, 11, 0);
        EventCreateRequestDto request = new EventCreateRequestDto(
                "Launch Day",
                "Big launch event",
                "banner.png",
                Event.EventType.PHYSICAL,
                "123 Main Street",
                null,
                start,
                end,
                LocalDateTime.of(2026, 10, 1, 9, 0),
                LocalDateTime.of(2026, 10, 9, 18, 0),
                false,
                true,
                200);

        EventResponseDto response = new EventResponseDto(
                1L,
                10L,
                "Acme Events",
                99L,
                "Manager User",
                "Launch Day",
                "Big launch event",
                "banner.png",
                Event.EventType.PHYSICAL,
                "123 Main Street",
                null,
                start,
                end,
                LocalDateTime.of(2026, 10, 1, 9, 0),
                LocalDateTime.of(2026, 10, 9, 18, 0),
                false,
                true,
                200,
                Event.Status.DRAFT,
                LocalDateTime.now(),
                LocalDateTime.now());

        when(eventOrganizerService.createEvent(any(EventCreateRequestDto.class), eq("organizer@example.com"))).thenReturn(response);

        mockMvc.perform(post("/api/events")
                        .principal(new UsernamePasswordAuthenticationToken("organizer@example.com", "pass"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Launch Day"));

        verify(eventOrganizerService).createEvent(any(EventCreateRequestDto.class), eq("organizer@example.com"));
    }

    @Test
    void getEventById_returnsEvent() throws Exception {
        LocalDateTime start = LocalDateTime.of(2026, 11, 12, 9, 0);
        LocalDateTime end = LocalDateTime.of(2026, 11, 12, 11, 0);
        EventResponseDto response = new EventResponseDto(
                2L,
                10L,
                "Acme Events",
                99L,
                "Manager User",
                "Innovation Expo",
                "Expo description",
                "expo.png",
                Event.EventType.VIRTUAL,
                null,
                "https://example.com/meet",
                start,
                end,
                LocalDateTime.of(2026, 11, 1, 9, 0),
                LocalDateTime.of(2026, 11, 10, 18, 0),
                false,
                false,
                150,
                Event.Status.PUBLISHED,
                LocalDateTime.now(),
                LocalDateTime.now());

        when(eventOrganizerService.getEventById(2L, "organizer@example.com")).thenReturn(response);

        mockMvc.perform(get("/api/events/{eventId}", 2L)
                        .principal(new UsernamePasswordAuthenticationToken("organizer@example.com", "pass")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Innovation Expo"));

        verify(eventOrganizerService).getEventById(2L, "organizer@example.com");
    }

    @Test
    void getEvents_returnsPage() throws Exception {
        LocalDateTime start = LocalDateTime.of(2026, 12, 5, 13, 0);
        LocalDateTime end = LocalDateTime.of(2026, 12, 5, 15, 0);
        EventResponseDto response = new EventResponseDto(
                3L,
                10L,
                "Acme Events",
                99L,
                "Manager User",
                "Year-End Gala",
                "Gala description",
                "gala.png",
                Event.EventType.PHYSICAL,
                "123 Main Street",
                null,
                start,
                end,
                LocalDateTime.of(2026, 11, 25, 9, 0),
                LocalDateTime.of(2026, 12, 3, 18, 0),
                false,
                true,
                100,
                Event.Status.PUBLISHED,
                LocalDateTime.now(),
                LocalDateTime.now());

        Page<EventResponseDto> page = new PageImpl<>(List.of(response), PageRequest.of(0, 10), 1);
        when(eventOrganizerService.getOrganizationEventsPaginated(any(), any(), any(), any(Pageable.class), eq("organizer@example.com")))
                .thenReturn(page);

        mockMvc.perform(get("/api/events")
                        .principal(new UsernamePasswordAuthenticationToken("organizer@example.com", "pass"))
                        .param("search", "gala"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("Year-End Gala"));

        verify(eventOrganizerService).getOrganizationEventsPaginated(any(), any(), any(), any(Pageable.class), eq("organizer@example.com"));
    }

    @Test
    void updateEvent_returnsUpdatedEvent() throws Exception {
        LocalDateTime start = LocalDateTime.of(2026, 9, 20, 9, 0);
        LocalDateTime end = LocalDateTime.of(2026, 9, 20, 11, 0);
        EventUpdateRequestDto request = new EventUpdateRequestDto("Updated Event", "Updated detail", null, null, null, null, start, end, false, true, 250, Event.Status.PUBLISHED);
        EventResponseDto response = new EventResponseDto(
                4L,
                10L,
                "Acme Events",
                99L,
                "Manager User",
                "Updated Event",
                "Updated detail",
                "banner.png",
                Event.EventType.PHYSICAL,
                "123 Main Street",
                null,
                start,
                end,
                LocalDateTime.of(2026, 9, 10, 9, 0),
                LocalDateTime.of(2026, 9, 18, 18, 0),
                false,
                true,
                250,
                Event.Status.PUBLISHED,
                LocalDateTime.now(),
                LocalDateTime.now());

        when(eventOrganizerService.updateEvent(4L, request, "organizer@example.com")).thenReturn(response);

        mockMvc.perform(put("/api/events/{eventId}", 4L)
                        .principal(new UsernamePasswordAuthenticationToken("organizer@example.com", "pass"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Event"));

        verify(eventOrganizerService).updateEvent(4L, request, "organizer@example.com");
    }

    @Test
    void deleteEvent_returnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/events/{eventId}", 5L)
                        .principal(new UsernamePasswordAuthenticationToken("organizer@example.com", "pass")))
                .andExpect(status().isNoContent());

        verify(eventOrganizerService).deleteEvent(5L, "organizer@example.com");
    }
}
