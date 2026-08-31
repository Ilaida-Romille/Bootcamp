package com.pointwest.bootcamp.eventhubri.modules.agenda.service;

import java.util.List;

import com.pointwest.bootcamp.eventhubri.modules.agenda.dto.AgendaCreateRequestDto;
import com.pointwest.bootcamp.eventhubri.modules.agenda.dto.AgendaResponseDto;
import com.pointwest.bootcamp.eventhubri.modules.agenda.dto.AgendaUpdateRequestDto;
import com.pointwest.bootcamp.eventhubri.modules.agenda.dto.SessionCreateRequestDto;
import com.pointwest.bootcamp.eventhubri.modules.agenda.dto.SessionResponseDto;
import com.pointwest.bootcamp.eventhubri.modules.agenda.dto.SessionUpdateRequestDto;
import com.pointwest.bootcamp.eventhubri.modules.agenda.dto.TrackCreateRequestDto;
import com.pointwest.bootcamp.eventhubri.modules.agenda.dto.TrackUpdateRequestDto;
import com.pointwest.bootcamp.eventhubri.modules.agenda.entity.Track;

public interface AgendaService {
    public AgendaResponseDto createAgenda(Long eventId, AgendaCreateRequestDto dto);

    public List<AgendaResponseDto> getAgendasByEventId(Long eventId);

    public AgendaResponseDto getAgendaById(Long agendaId);

    AgendaResponseDto updateAgenda(Long agendaId, AgendaUpdateRequestDto dto);

    void deleteAgenda(Long agendaId);

    public AgendaResponseDto.TrackSummary createTrack(Long agendaId, TrackCreateRequestDto dto);

    AgendaResponseDto.TrackSummary updateTrack(Long trackId, TrackUpdateRequestDto dto);

    void deleteTrack(Long trackId);

    public SessionResponseDto createSession(SessionCreateRequestDto dto);

    SessionResponseDto updateSession(Long sessionId, SessionUpdateRequestDto dto);

    void deleteSession(Long sessionId);
}
