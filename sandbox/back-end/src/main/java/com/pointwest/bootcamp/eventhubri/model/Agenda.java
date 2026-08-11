import java.util.ArrayList;
import java.util.List;

public class Agenda {
    private String agendaId;
    private String description;
    private List<Session> sessions;

    public Agenda() {
        this.sessions = new ArrayList<>();
    }

    public Agenda(String agendaId, String description) {
        this.agendaId = agendaId;
        this.description = description;
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

    public List<Session> getSessions() {
        return sessions;
    }

    public void setSessions(List<Session> sessions) {
        this.sessions = sessions;
    }

    public void addSession(Session session) {
        this.sessions.add(session);
    }
}