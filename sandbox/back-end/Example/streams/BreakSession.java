import java.time.LocalDateTime;

class BreakSession extends Session {
    private String breakType; // e.g., "Lunch", "Coffee Break"

    public BreakSession(String agendaItemId, LocalDateTime startDateTime, LocalDateTime endDateTime, String title, String description, String location, String breakType) {
        super(agendaItemId, startDateTime, endDateTime, title, description, location);
        this.breakType = breakType;
    }

    public String getBreakType() { return breakType; }
    public void setBreakType(String breakType) { this.breakType = breakType; }
}