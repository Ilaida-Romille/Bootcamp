import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
// ==========================================
// 2. AGENDA (Contains List of Sessions + Streams)
// ==========================================
class Agenda {
    private String description;
    private List<Session> sessions = new ArrayList<>();

    public Agenda(String description) {
        this.description = description;
    }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<Session> getSessions() { return sessions; }
    public void addSession(Session session) { this.sessions.add(session); }

    // --- Stream Operations on Sessions ---

    // Filter only presentation sessions
    public List<PresentationSession> getPresentationSessions() {
        return sessions.stream()
                .filter(PresentationSession.class::isInstance)
                .map(PresentationSession.class::cast)
                .collect(Collectors.toList());
    }

    // Filter sessions by speaker name
    public List<PresentationSession> findSessionsBySpeaker(String speakerName) {
        return getPresentationSessions().stream()
                .filter(s -> s.getSpeaker().equalsIgnoreCase(speakerName))
                .collect(Collectors.toList());
    }

    // Sum total duration of all breaks using mapToLong & sum
    public long getTotalBreakDurationMinutes() {
        return sessions.stream()
                .filter(BreakSession.class::isInstance)
                .mapToLong(Session::getDurationInMinutes)
                .sum();
    }

    // Return sessions sorted by start time
    public List<Session> getSessionsChronologically() {
        return sessions.stream()
                .sorted(Comparator.comparing(Session::getStartDateTime))
                .collect(Collectors.toList());
    }
}