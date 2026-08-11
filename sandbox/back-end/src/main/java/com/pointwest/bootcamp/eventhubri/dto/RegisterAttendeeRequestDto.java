import java.util.ArrayList;
import java.util.List;

public class RegisterAttendeeRequestDto {
    private String attendeeId;
    private String eventId;
    private String dietaryRestrictions;
    private List<String> selectedSessionIds;

    public RegisterAttendeeRequestDto() {
        this.selectedSessionIds = new ArrayList<>();
    }

    public RegisterAttendeeRequestDto(String attendeeId, String eventId, String dietaryRestrictions, List<String> selectedSessionIds) {
        this.attendeeId = attendeeId;
        this.eventId = eventId;
        this.dietaryRestrictions = dietaryRestrictions;
        this.selectedSessionIds = selectedSessionIds != null ? selectedSessionIds : new ArrayList<>();
    }

    public String getAttendeeId() {
        return attendeeId;
    }

    public void setAttendeeId(String attendeeId) {
        this.attendeeId = attendeeId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getDietaryRestrictions() {
        return dietaryRestrictions;
    }

    public void setDietaryRestrictions(String dietaryRestrictions) {
        this.dietaryRestrictions = dietaryRestrictions;
    }

    public List<String> getSelectedSessionIds() {
        return selectedSessionIds;
    }

    public void setSelectedSessionIds(List<String> selectedSessionIds) {
        this.selectedSessionIds = selectedSessionIds;
    }
}