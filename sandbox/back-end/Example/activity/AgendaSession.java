import java.util.ArrayList;
import java.util.List;

/**
 * Single Responsibility Principle (SRP):
 * Responsible only for maintaining and managing the list of sessions for an event.
 */
public class Agenda {
    public String description;
    private List<Session> sessions = new ArrayList<>();

    public Agenda(String description) {
        this.description = description;
    }

    public void addSession(Session session) {
        sessions.add(session);
        System.out.println("[Agenda] Session added: " + session.title);
    }

    public List<Session> getSessions() {
        return sessions;
    }

    public void printAgenda() {
        System.out.println("=== Agenda: " + description + " ===");
        for (Session s : sessions) {
            s.displayInfo(); // Polymorphic call (LSP in action)
        }
    }
}