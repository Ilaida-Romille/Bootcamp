import java.util.List;

public interface AgendaService {
    AgendaResponseDto getAgendaByEventId(String eventId);
    SessionResponseDto addSessionToEvent(String eventId, Session session);
}