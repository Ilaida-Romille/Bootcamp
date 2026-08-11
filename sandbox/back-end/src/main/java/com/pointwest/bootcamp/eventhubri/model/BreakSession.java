import java.time.LocalDateTime;

public class BreakSession extends Session {
    private String breakType; // e.g., "Lunch", "Coffee Break"

    public BreakSession() {
        super();
    }

    public BreakSession(String sessionId, String title, String description, 
                        LocalDateTime startDateTime, LocalDateTime endDateTime, 
                        String location, String breakType) {
        super(sessionId, title, description, startDateTime, endDateTime, location);
        this.breakType = breakType;
    }

    public String getBreakType() {
        return breakType;
    }

    public void setBreakType(String breakType) {
        this.breakType = breakType;
    }
}