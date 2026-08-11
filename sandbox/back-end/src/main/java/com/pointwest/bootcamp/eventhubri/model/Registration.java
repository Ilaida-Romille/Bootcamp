import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Registration {
    private String registrationId;
    private Attendee attendee;
    private Event event;
    private LocalDateTime registeredAt;
    private RegistrationStatus status;
    private String dietaryRestrictions;
    private List<SessionSelection> sessionSelections;

    public Registration() {
        this.registeredAt = LocalDateTime.now();
        this.status = RegistrationStatus.PENDING;
        this.sessionSelections = new ArrayList<>();
    }

    public Registration(String registrationId, Attendee attendee, Event event, String dietaryRestrictions) {
        this.registrationId = registrationId;
        this.attendee = attendee;
        this.event = event;
        this.dietaryRestrictions = dietaryRestrictions;
        this.registeredAt = LocalDateTime.now();
        this.status = RegistrationStatus.CONFIRMED;
        this.sessionSelections = new ArrayList<>();
    }

    public String getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }

    public Attendee getAttendee() {
        return attendee;
    }

    public void setAttendee(Attendee attendee) {
        this.attendee = attendee;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
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

    public List<SessionSelection> getSessionSelections() {
        return sessionSelections;
    }

    public void setSessionSelections(List<SessionSelection> sessionSelections) {
        this.sessionSelections = sessionSelections;
    }

    public void addSessionSelection(SessionSelection selection) {
        this.sessionSelections.add(selection);
    }
}