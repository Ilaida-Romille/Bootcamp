import java.time.LocalDateTime;

class PresentationSession extends Session {
    private String speaker;

    public PresentationSession(String agendaItemId, LocalDateTime startDateTime, LocalDateTime endDateTime, String title, String description, String location, String speaker) {
        super(agendaItemId, startDateTime, endDateTime, title, description, location);
        this.speaker = speaker;
    }

    public String getSpeaker() { return speaker; }
    public void setSpeaker(String speaker) { this.speaker = speaker; }
}