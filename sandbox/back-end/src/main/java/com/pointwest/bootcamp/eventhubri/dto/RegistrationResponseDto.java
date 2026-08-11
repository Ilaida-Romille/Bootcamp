import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RegistrationResponseDto {
    private String registrationId;
    private String attendeeId;
    private String attendeeName;
    private String attendeeEmail;
    private String eventId;
    private String eventTitle;
    private LocalDateTime registeredAt;
    private RegistrationStatus status;
    private String dietaryRestrictions;
    private List<SessionResponseDto> selectedSessions;

    public RegistrationResponseDto() {
        this.selectedSessions = new ArrayList<>();
    }

    public String getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }

    public String getAttendeeId() {
        return attendeeId;
    }

    public void setAttendeeId(String attendeeId) {
        this.attendeeId = attendeeId;
    }

    public String getAttendeeName() {
        return attendeeName;
    }

    public void setAttendeeName(String attendeeName) {
        this.attendeeName = attendeeName;
    }

    public String getAttendeeEmail() {
        return attendeeEmail;
    }

    public void setAttendeeEmail(String attendeeEmail) {
        this.attendeeEmail = attendeeEmail;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public LocalDateTime getRegisteredAt() {
        return registeredAt;
    }

    public void setRegisteredAt(LocalDateTime registeredAt) {
        this.registeredAt = registeredAt;
    }

    public RegistrationStatus getStatus() {
        return status;
    }

    public void setStatus(RegistrationStatus status) {
        this.status = status;
    }

    public String getDietaryRestrictions() {
        return dietaryRestrictions;
    }

    public void setDietaryRestrictions(String dietaryRestrictions) {
        this.dietaryRestrictions = dietaryRestrictions;
    }

    public List<SessionResponseDto> getSelectedSessions() {
        return selectedSessions;
    }

    public void setSelectedSessions(List<SessionResponseDto> selectedSessions) {
        this.selectedSessions = selectedSessions;
    }
}