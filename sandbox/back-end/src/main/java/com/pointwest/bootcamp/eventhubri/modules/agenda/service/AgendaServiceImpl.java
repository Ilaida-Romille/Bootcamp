package com.pointwest.bootcamp.eventhubri.modules.agenda.service;

import java.util.HashSet;
import java.util.List;

import org.springframework.stereotype.Service;

import com.pointwest.bootcamp.eventhubri.modules.agenda.dto.AgendaCreateRequestDto;
import com.pointwest.bootcamp.eventhubri.modules.agenda.dto.AgendaResponseDto;
import com.pointwest.bootcamp.eventhubri.modules.agenda.dto.AgendaUpdateRequestDto;
import com.pointwest.bootcamp.eventhubri.modules.agenda.dto.SessionCreateRequestDto;
import com.pointwest.bootcamp.eventhubri.modules.agenda.dto.SessionResponseDto;
import com.pointwest.bootcamp.eventhubri.modules.agenda.dto.SessionUpdateRequestDto;
import com.pointwest.bootcamp.eventhubri.modules.agenda.dto.TrackCreateRequestDto;
import com.pointwest.bootcamp.eventhubri.modules.agenda.dto.TrackUpdateRequestDto;
import com.pointwest.bootcamp.eventhubri.modules.agenda.entity.Agenda;
import com.pointwest.bootcamp.eventhubri.modules.agenda.entity.Session;
import com.pointwest.bootcamp.eventhubri.modules.agenda.entity.Speaker;
import com.pointwest.bootcamp.eventhubri.modules.agenda.entity.Track;
import com.pointwest.bootcamp.eventhubri.modules.agenda.repository.AgendaRepository;
import com.pointwest.bootcamp.eventhubri.modules.agenda.repository.SessionRepository;
import com.pointwest.bootcamp.eventhubri.modules.agenda.repository.SpeakerRepository;
import com.pointwest.bootcamp.eventhubri.modules.agenda.repository.TrackRepository;
import com.pointwest.bootcamp.eventhubri.modules.event.entity.Event;
import com.pointwest.bootcamp.eventhubri.modules.event.repository.EventRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AgendaServiceImpl implements AgendaService {

        private final AgendaRepository agendaRepository;
        private final TrackRepository trackRepository;
        private final SessionRepository sessionRepository;
        private final SpeakerRepository speakerRepository;
        private final EventRepository eventRepository;

        @Override
        public AgendaResponseDto createAgenda(Long eventId, AgendaCreateRequestDto dto) {
                Event event = eventRepository.findById(eventId)
                                .orElseThrow(() -> new EntityNotFoundException("Event not found with ID: " + eventId));

                Agenda agenda = Agenda.builder()
                                .event(event)
                                .agendaDate(dto.agendaDate())
                                .title(dto.title())
                                .description(dto.description())
                                .build();

                Agenda savedAgenda = agendaRepository.save(agenda);
                return mapToAgendaResponseDto(savedAgenda);
        }

        @Override
        public SessionResponseDto createSession(SessionCreateRequestDto dto) {
                Track track = trackRepository.findById(dto.trackId())
                                .orElseThrow(() -> new EntityNotFoundException(
                                                "Track not found with ID: " + dto.trackId()));

                List<Speaker> speakers = (dto.speakerIds() != null && !dto.speakerIds().isEmpty())
                                ? speakerRepository.findAllById(dto.speakerIds())
                                : List.of();

                Session session = Session.builder()
                                .track(track)
                                .title(dto.title())
                                .description(dto.description())
                                .startTime(dto.startTime())
                                .endTime(dto.endTime())
                                .locationOrRoom(dto.locationOrRoom())
                                .speakers(new HashSet<>(speakers))
                                .build();

                Session savedSession = sessionRepository.save(session);
                return mapToSessionResponseDto(savedSession);
        }

        @Override
        public AgendaResponseDto.TrackSummary createTrack(Long agendaId, TrackCreateRequestDto dto) {
                Agenda agenda = agendaRepository.findById(agendaId)
                                .orElseThrow(() -> new EntityNotFoundException(
                                                "Agenda not found with ID: " + agendaId));

                Track track = Track.builder()
                                .agenda(agenda)
                                .name(dto.name())
                                .description(dto.description())
                                .displayOrder(dto.displayOrder())
                                .build();

                Track savedTrack = trackRepository.save(track);

                return new AgendaResponseDto.TrackSummary(
                                savedTrack.getId(),
                                savedTrack.getName(),
                                savedTrack.getDescription(),
                                savedTrack.getDisplayOrder(),
                                List.of());
        }

        @Override
        public AgendaResponseDto getAgendaById(Long agendaId) {
                Agenda agenda = agendaRepository.findById(agendaId)
                                .orElseThrow(() -> new EntityNotFoundException(
                                                "Agenda not found with ID: " + agendaId));
                return mapToAgendaResponseDto(agenda);
        }

        @Override
        public List<AgendaResponseDto> getAgendasByEventId(Long eventId) {
                return agendaRepository.findByEventIdOrderByAgendaDateAsc(eventId).stream()
                                .map(this::mapToAgendaResponseDto)
                                .toList();
        }

        @Override
        public void deleteAgenda(Long agendaId) {
                if (!agendaRepository.existsById(agendaId)) {
                        throw new EntityNotFoundException("Agenda not found with ID: " + agendaId);
                }
                agendaRepository.deleteById(agendaId);
        }

        @Override
        public void deleteSession(Long sessionId) {
                if (!sessionRepository.existsById(sessionId)) {
                        throw new EntityNotFoundException("Session not found with ID: " + sessionId);
                }
                sessionRepository.deleteById(sessionId);
        }

        @Override
        public void deleteTrack(Long trackId) {
                if (!trackRepository.existsById(trackId)) {
                        throw new EntityNotFoundException("Track not found with ID: " + trackId);
                }
                trackRepository.deleteById(trackId);
        }

        @Override
        public AgendaResponseDto updateAgenda(Long agendaId, AgendaUpdateRequestDto dto) {
                Agenda agenda = agendaRepository.findById(agendaId)
                                .orElseThrow(() -> new EntityNotFoundException(
                                                "Agenda not found with ID: " + agendaId));

                agenda.setAgendaDate(dto.agendaDate());
                agenda.setTitle(dto.title());
                agenda.setDescription(dto.description());

                return mapToAgendaResponseDto(agendaRepository.save(agenda));
        }

        @Override
        public SessionResponseDto updateSession(Long sessionId, SessionUpdateRequestDto dto) {
                Session session = sessionRepository.findById(sessionId)
                                .orElseThrow(() -> new EntityNotFoundException(
                                                "Session not found with ID: " + sessionId));

                if (dto.trackId() != null) {
                        Track track = trackRepository.findById(dto.trackId())
                                        .orElseThrow(() -> new EntityNotFoundException(
                                                        "Track not found with ID: " + dto.trackId()));
                        session.setTrack(track);
                }

                session.setTitle(dto.title());
                session.setDescription(dto.description());
                session.setStartTime(dto.startTime());
                session.setEndTime(dto.endTime());
                session.setLocationOrRoom(dto.locationOrRoom());

                if (dto.speakerIds() != null) {
                        List<Speaker> speakers = speakerRepository.findAllById(dto.speakerIds());
                        session.setSpeakers(new HashSet<>(speakers));
                }

                return mapToSessionResponseDto(sessionRepository.save(session));
        }

        @Override
        public AgendaResponseDto.TrackSummary updateTrack(Long trackId, TrackUpdateRequestDto dto) {
                Track track = trackRepository.findById(trackId)
                                .orElseThrow(() -> new EntityNotFoundException(
                                                "Track not found with ID: " + trackId));

                track.setName(dto.name());
                track.setDescription(dto.description());

                if (dto.displayOrder() != null) {
                        track.setDisplayOrder(dto.displayOrder());
                }

                Track savedTrack = trackRepository.save(track);

                List<Session> sessions = sessionRepository.findByTrackIdOrderByStartTimeAsc(savedTrack.getId());

                List<AgendaResponseDto.SessionSummary> sessionSummaries = sessions.stream()
                                .map(session -> new AgendaResponseDto.SessionSummary(
                                                session.getId(),
                                                session.getTitle(),
                                                session.getStartTime(),
                                                session.getEndTime(),
                                                session.getLocationOrRoom()))
                                .toList();

                return new AgendaResponseDto.TrackSummary(
                                savedTrack.getId(),
                                savedTrack.getName(),
                                savedTrack.getDescription(),
                                savedTrack.getDisplayOrder(),
                                sessionSummaries);
        }

        private AgendaResponseDto mapToAgendaResponseDto(Agenda agenda) {
                List<Track> tracks = trackRepository.findByAgendaIdOrderByDisplayOrderAsc(agenda.getId());

                List<AgendaResponseDto.TrackSummary> trackSummaries = tracks.stream().map(track -> {
                        List<Session> sessions = sessionRepository.findByTrackIdOrderByStartTimeAsc(track.getId());

                        List<AgendaResponseDto.SessionSummary> sessionSummaries = sessions.stream()
                                        .map(s -> new AgendaResponseDto.SessionSummary(
                                                        s.getId(), s.getTitle(), s.getStartTime(), s.getEndTime(),
                                                        s.getLocationOrRoom()))
                                        .toList();

                        return new AgendaResponseDto.TrackSummary(
                                        track.getId(), track.getName(), track.getDescription(), track.getDisplayOrder(),
                                        sessionSummaries);
                }).toList();

                return new AgendaResponseDto(
                                agenda.getId(),
                                agenda.getEvent().getId(),
                                agenda.getAgendaDate(),
                                agenda.getTitle(),
                                agenda.getDescription(),
                                trackSummaries);
        }

        private SessionResponseDto mapToSessionResponseDto(Session session) {
                List<SessionResponseDto.SpeakerSummary> speakerSummaries = session.getSpeakers().stream()
                                .map(sp -> new SessionResponseDto.SpeakerSummary(
                                                sp.getId(), sp.getFullName(), sp.getOrganizationOrTitle(),
                                                sp.getPhotoUrl()))
                                .toList();

                return new SessionResponseDto(
                                session.getId(),
                                session.getTrack().getId(),
                                session.getTitle(),
                                session.getDescription(),
                                session.getStartTime(),
                                session.getEndTime(),
                                session.getLocationOrRoom(),
                                speakerSummaries);
        }
}
