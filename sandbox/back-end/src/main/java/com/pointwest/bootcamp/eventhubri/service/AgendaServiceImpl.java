import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AgendaServiceImpl implements AgendaService {

    private final EventRepository eventRepository;

    public AgendaServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public AgendaResponseDto getAgendaByEventId(String eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found with ID: " + eventId));

        Agenda agenda = event.getAgenda();
        if (agenda == null) {
            return new AgendaResponseDto();
        }

        return mapToAgendaResponseDto(agenda);
    }

    @Override
    public SessionResponseDto addSessionToEvent(String eventId, Session session) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found with ID: " + eventId));

        Agenda agenda = event.getAgenda();
        if (agenda == null) {
            agenda = new Agenda(java.util.UUID.randomUUID().toString(), "Agenda for " + event.getTitle());
            event.setAgenda(agenda);
        }

        agenda.addSession(session);
        eventRepository.save(event);

        return mapToSessionResponseDto(session);
    }

    // Mapper Helper Methods
    private AgendaResponseDto mapToAgendaResponseDto(Agenda agenda) {
        AgendaResponseDto dto = new AgendaResponseDto();
        dto.setAgendaId(agenda.getAgendaId());
        dto.setDescription(agenda.getDescription());

        List<SessionResponseDto> sessionDtos = new ArrayList<>();
        if (agenda.getSessions() != null) {
            for (Session session : agenda.getSessions()) {
                sessionDtos.add(mapToSessionResponseDto(session));
            }
        }
        dto.setSessions(sessionDtos);
        return dto;
    }

    private SessionResponseDto mapToSessionResponseDto(Session session) {
        SessionResponseDto dto = new SessionResponseDto();
        dto.setSessionId(session.getSessionId());
        dto.setTitle(session.getTitle());
        dto.setDescription(session.getDescription());
        dto.setStartDateTime(session.getStartDateTime());
        dto.setEndDateTime(session.getEndDateTime());
        dto.setLocation(session.getLocation());

        if (session instanceof PresentationSession) {
            PresentationSession presentation = (PresentationSession) session;
            dto.setSessionType("PRESENTATION");
            dto.setSpeaker(presentation.getSpeaker());
        } else if (session instanceof BreakSession) {
            BreakSession breakSession = (BreakSession) session;
            dto.setSessionType("BREAK");
            dto.setBreakType(breakSession.getBreakType());
        }

        return dto;
    }
}