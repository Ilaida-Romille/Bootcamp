package com.pointwest.bootcamp.eventhubri.modules.agenda.controller;

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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pointwest.bootcamp.eventhubri.modules.agenda.dto.AgendaCreateRequestDto;
import com.pointwest.bootcamp.eventhubri.modules.agenda.dto.AgendaResponseDto;
import com.pointwest.bootcamp.eventhubri.modules.agenda.dto.AgendaUpdateRequestDto;
import com.pointwest.bootcamp.eventhubri.modules.agenda.dto.SessionCreateRequestDto;
import com.pointwest.bootcamp.eventhubri.modules.agenda.dto.SessionResponseDto;
import com.pointwest.bootcamp.eventhubri.modules.agenda.dto.SessionUpdateRequestDto;
import com.pointwest.bootcamp.eventhubri.modules.agenda.dto.TrackCreateRequestDto;
import com.pointwest.bootcamp.eventhubri.modules.agenda.dto.TrackUpdateRequestDto;
import com.pointwest.bootcamp.eventhubri.modules.agenda.service.AgendaService;

@ExtendWith(MockitoExtension.class)
class AgendaControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private AgendaService agendaService;

    @InjectMocks
    private AgendaController controller;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        objectMapper.findAndRegisterModules();
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    void createAgenda_returnsCreatedAgenda() throws Exception {
        AgendaCreateRequestDto request = new AgendaCreateRequestDto(LocalDate.of(2026, 9, 10), "Opening Session", "Welcome remarks");
        AgendaResponseDto response = new AgendaResponseDto(1L, 99L, request.agendaDate(), request.title(), request.description(), List.of());

        when(agendaService.createAgenda(99L, request)).thenReturn(response);

        mockMvc.perform(post("/api/events/{eventId}/agendas", 99L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Opening Session"));

        verify(agendaService).createAgenda(99L, request);
    }

    @Test
    void getAgendasByEvent_returnsAgendaList() throws Exception {
        AgendaResponseDto response = new AgendaResponseDto(
                2L,
                77L,
                LocalDate.of(2026, 9, 11),
                "Track Day",
                "Agenda body",
                List.of());

        when(agendaService.getAgendasByEventId(77L)).thenReturn(List.of(response));

        mockMvc.perform(get("/api/events/{eventId}/agendas", 77L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Track Day"));

        verify(agendaService).getAgendasByEventId(77L);
    }

    @Test
    void getAgendaById_returnsAgenda() throws Exception {
        AgendaResponseDto response = new AgendaResponseDto(
                3L,
                88L,
                LocalDate.of(2026, 9, 12),
                "Workshop",
                "Agenda details",
                List.of());

        when(agendaService.getAgendaById(3L)).thenReturn(response);

        mockMvc.perform(get("/api/events/agendas/{agendaId}", 3L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3));

        verify(agendaService).getAgendaById(3L);
    }

    @Test
    void updateAgenda_returnsUpdatedAgenda() throws Exception {
        AgendaUpdateRequestDto request = new AgendaUpdateRequestDto(LocalDate.of(2026, 9, 15), "Updated Agenda", "Updated description");
        AgendaResponseDto response = new AgendaResponseDto(4L, 99L, request.agendaDate(), request.title(), request.description(), List.of());

        when(agendaService.updateAgenda(4L, request)).thenReturn(response);

        mockMvc.perform(put("/api/events/agendas/{agendaId}", 4L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Agenda"));

        verify(agendaService).updateAgenda(4L, request);
    }

    @Test
    void deleteAgenda_returnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/events/agendas/{agendaId}", 5L))
                .andExpect(status().isNoContent());

        verify(agendaService).deleteAgenda(5L);
    }

    @Test
    void createTrack_returnsTrackSummary() throws Exception {
        TrackCreateRequestDto request = new TrackCreateRequestDto("Track One", "Main track", 1);
        AgendaResponseDto.TrackSummary response = new AgendaResponseDto.TrackSummary(10L, "Track One", "Main track", 1, List.of());

        when(agendaService.createTrack(8L, request)).thenReturn(response);

        mockMvc.perform(post("/api/events/agendas/{agendaId}/tracks", 8L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Track One"));

        verify(agendaService).createTrack(8L, request);
    }

    @Test
    void updateTrack_returnsUpdatedTrackSummary() throws Exception {
        TrackUpdateRequestDto request = new TrackUpdateRequestDto("Updated Track", "Updated description", 2);
        AgendaResponseDto.TrackSummary response = new AgendaResponseDto.TrackSummary(11L, "Updated Track", "Updated description", 2, List.of());

        when(agendaService.updateTrack(11L, request)).thenReturn(response);

        mockMvc.perform(put("/api/events/tracks/{trackId}", 11L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Updated description"));

        verify(agendaService).updateTrack(11L, request);
    }

    @Test
    void deleteTrack_returnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/events/tracks/{trackId}", 12L))
                .andExpect(status().isNoContent());

        verify(agendaService).deleteTrack(12L);
    }

    @Test
    void createSession_returnsSession() throws Exception {
        LocalDateTime start = LocalDateTime.of(2026, 9, 13, 9, 0);
        LocalDateTime end = LocalDateTime.of(2026, 9, 13, 10, 0);
        SessionCreateRequestDto request = new SessionCreateRequestDto(15L, "Keynote", "Welcome", start, end, "Main Hall", List.of());
        SessionResponseDto response = new SessionResponseDto(20L, 15L, "Keynote", "Welcome", start, end, "Main Hall", List.of());

        when(agendaService.createSession(any(SessionCreateRequestDto.class))).thenReturn(response);

        mockMvc.perform(post("/api/events/tracks/{trackId}/sessions", 15L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Keynote"));

        verify(agendaService).createSession(any(SessionCreateRequestDto.class));
    }

    @Test
    void updateSession_returnsUpdatedSession() throws Exception {
        LocalDateTime start = LocalDateTime.of(2026, 9, 14, 11, 0);
        LocalDateTime end = LocalDateTime.of(2026, 9, 14, 12, 0);
        SessionUpdateRequestDto request = new SessionUpdateRequestDto(15L, "Updated Session", "Updated desc", start, end, "Room A", List.of());
        SessionResponseDto response = new SessionResponseDto(21L, 15L, "Updated Session", "Updated desc", start, end, "Room A", List.of());

        when(agendaService.updateSession(21L, request)).thenReturn(response);

        mockMvc.perform(put("/api/events/sessions/{sessionId}", 21L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.locationOrRoom").value("Room A"));

        verify(agendaService).updateSession(21L, request);
    }

    @Test
    void deleteSession_returnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/events/sessions/{sessionId}", 22L))
                .andExpect(status().isNoContent());

        verify(agendaService).deleteSession(22L);
    }
}
