import java.time.Duration;
import java.time.LocalDateTime;
// ==========================================
// 1. SESSION (Abstract) & Subclasses
// ==========================================
abstract class Session {
    private String agendaItemId;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private String title;
    private String description;
    private String location;

    public Session(String agendaItemId, LocalDateTime startDateTime, LocalDateTime endDateTime, String title, String description, String location) {
        this.agendaItemId = agendaItemId;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.title = title;
        this.description = description;
        this.location = location;
    }

    public String getAgendaItemId() { return agendaItemId; }
    public void setAgendaItemId(String agendaItemId) { this.agendaItemId = agendaItemId; }

    public LocalDateTime getStartDateTime() { return startDateTime; }
    public void setStartDateTime(LocalDateTime startDateTime) { this.startDateTime = startDateTime; }

    public LocalDateTime getEndDateTime() { return endDateTime; }
    public void setEndDateTime(LocalDateTime endDateTime) { this.endDateTime = endDateTime; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public long getDurationInMinutes() {
        return Duration.between(startDateTime, endDateTime).toMinutes();
    }

    @Override
    public String toString() {
        return String.format("[%s] %s (%s to %s)", getClass().getSimpleName(), title, startDateTime, endDateTime);
    }
}
