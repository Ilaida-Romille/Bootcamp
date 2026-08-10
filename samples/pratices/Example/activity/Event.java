import java.util.Date;

/**
 * Single Responsibility Principle (SRP):
 * Encapsulates domain details and state for an Event.
 * Does not concern itself with persistence logic or UI formatting.
 */
public class Event {
    private String eventId;
    public String title;
    public String description;
    public String organizerId;
    public String organizerName;
    public String status;
    public Date startDateTime;
    public Date endDateTime;
    public Date registrationOpensAt;
    public Date registrationClosesAt;
    public String venue;
    public String bannerImageUrl;
    public String capacity;
    public Agenda agenda;

    public Event(String eventId, String title, String description, String organizerId, String organizerName) {
        this.eventId = eventId;
        this.title = title;
        this.description = description;
        this.organizerId = organizerId;
        this.organizerName = organizerName;
        this.status = "DRAFT";
    }

    public String getEventId() {
        return eventId;
    }
}