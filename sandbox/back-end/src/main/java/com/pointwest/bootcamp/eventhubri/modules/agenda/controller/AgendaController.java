package com.pointwest.bootcamp.eventhubri.modules.agenda.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pointwest.bootcamp.eventhubri.modules.agenda.dto.AgendaCreateRequestDto;
import com.pointwest.bootcamp.eventhubri.modules.agenda.dto.AgendaResponseDto;
import com.pointwest.bootcamp.eventhubri.modules.agenda.dto.AgendaUpdateRequestDto;
import com.pointwest.bootcamp.eventhubri.modules.agenda.dto.SessionCreateRequestDto;
import com.pointwest.bootcamp.eventhubri.modules.agenda.dto.SessionResponseDto;
import com.pointwest.bootcamp.eventhubri.modules.agenda.dto.SessionUpdateRequestDto;
import com.pointwest.bootcamp.eventhubri.modules.agenda.dto.TrackCreateRequestDto;
import com.pointwest.bootcamp.eventhubri.modules.agenda.dto.TrackUpdateRequestDto;
import com.pointwest.bootcamp.eventhubri.modules.agenda.entity.Track;
import com.pointwest.bootcamp.eventhubri.modules.agenda.service.AgendaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class AgendaController {

    private final AgendaService agendaService;

    // Agenda
    @PostMapping("/{eventId}/agendas")
    public ResponseEntity<AgendaResponseDto> createAgenda(
            @PathVariable Long eventId,
            @Valid @RequestBody AgendaCreateRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(agendaService.createAgenda(eventId, dto));
    }

    @GetMapping("/{eventId}/agendas")
    public ResponseEntity<List<AgendaResponseDto>> getAgendasByEvent(@PathVariable Long eventId) {
        return ResponseEntity.ok(agendaService.getAgendasByEventId(eventId));
    }

    @GetMapping("/agendas/{agendaId}")
    public ResponseEntity<AgendaResponseDto> getAgendaById(@PathVariable Long agendaId) {
        return ResponseEntity.ok(agendaService.getAgendaById(agendaId));
    }

    @PutMapping("/agendas/{agendaId}")
    public ResponseEntity<AgendaResponseDto> updateAgenda(
            @PathVariable Long agendaId,
            @Valid @RequestBody AgendaUpdateRequestDto dto) {
        return ResponseEntity.ok(agendaService.updateAgenda(agendaId, dto));
    }

    @DeleteMapping("/agendas/{agendaId}")
    public ResponseEntity<Void> deleteAgenda(@PathVariable Long agendaId) {
        agendaService.deleteAgenda(agendaId);
        return ResponseEntity.noContent().build();
    }

    // Tracks
    @PostMapping("/agendas/{agendaId}/tracks")
    public ResponseEntity<AgendaResponseDto.TrackSummary> createTrack(
            @PathVariable Long agendaId,
            @Valid @RequestBody TrackCreateRequestDto dto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(agendaService.createTrack(agendaId, dto));
    }

    @PutMapping("/tracks/{trackId}")
    public ResponseEntity<AgendaResponseDto.TrackSummary> updateTrack(
            @PathVariable Long trackId,
            @Valid @RequestBody TrackUpdateRequestDto dto) {
        return ResponseEntity.ok(agendaService.updateTrack(trackId, dto));
    }

    @DeleteMapping("/tracks/{trackId}")
    public ResponseEntity<Void> deleteTrack(@PathVariable Long trackId) {
        agendaService.deleteTrack(trackId);
        return ResponseEntity.noContent().build();
    }

    // Sessions

    @PostMapping("/tracks/{trackId}/sessions")
    public ResponseEntity<SessionResponseDto> createSession(
            @PathVariable Long trackId,
            @Valid @RequestBody SessionCreateRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(agendaService.createSession(dto));
    }

    @PutMapping("/sessions/{sessionId}")
    public ResponseEntity<SessionResponseDto> updateSession(
            @PathVariable Long sessionId,
            @Valid @RequestBody SessionUpdateRequestDto dto) {
        return ResponseEntity.ok(agendaService.updateSession(sessionId, dto));
    }

    @DeleteMapping("/sessions/{sessionId}")
    public ResponseEntity<Void> deleteSession(@PathVariable Long sessionId) {
        agendaService.deleteSession(sessionId);
        return ResponseEntity.noContent().build();
    }

}
