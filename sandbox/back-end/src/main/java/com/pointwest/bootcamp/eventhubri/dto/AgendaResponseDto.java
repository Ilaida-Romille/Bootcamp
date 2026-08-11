import java.util.ArrayList;
import java.util.List;

public class AgendaResponseDto {
    private String agendaId;
    private String description;
    private List<SessionResponseDto> sessions;

    public AgendaResponseDto() {
        this.sessions = new ArrayList<>();
    }

    public String getAgendaId() {
        return agendaId;
    }

    public void setAgendaId(String agendaId) {
        this.agendaId = agendaId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<SessionResponseDto> getSessions() {
        return sessions;
    }

    public void setSessions(List<SessionResponseDto> sessions) {
        this.sessions = sessions;
    }
}