import java.time.LocalDateTime;

public class PresentationSession extends Session {
    private String speaker;

    public PresentationSession() {
        super();
    }

    public PresentationSession(String sessionId, String title, String description, 
                               LocalDateTime startDateTime, LocalDateTime endDateTime, 
                               String location, String speaker) {
        super(sessionId, title, description, startDateTime, endDateTime, location);
        this.speaker = speaker;
    }

    public String getSpeaker() {
        return speaker;
    }

    public void setSpeaker(String speaker) {
        this.speaker = speaker;
    }
}