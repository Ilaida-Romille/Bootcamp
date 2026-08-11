import org.springframework.stereotype.Component;
import com.pointwest.bootcamp.eventhubri.service.agendaService;

@Controller
public class AgendaController {

    private final AgendaService agendaService;

    public AgendaController(AgendaService agendaService) {
        this.agendaService = agendaService;
    }

    public AgendaResponseDto getAgendaByEventId(String eventId) {
        return agendaService.getAgendaByEventId(eventId);
    }

    public SessionResponseDto addSessionToEvent(String eventId, Session session) {
        return agendaService.addSessionToEvent(eventId, session);
    }
}