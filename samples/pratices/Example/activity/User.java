/**
 * Single Responsibility Principle (SRP):
 * Represents a regular attendee viewing events via the repository.
 */
public class User {
    private String attendeeId;
    public String name;
    public String email;

    public User(String attendeeId, String name, String email) {
        this.attendeeId = attendeeId;
        this.name = name;
        this.email = email;
    }

    public Event viewEvent(EventRepository repository, String eventId) {
        System.out.println("[User: " + name + "] Viewing event...");
        return repository.getEvent(eventId);
    }

    public String getAttendeeId() {
        return attendeeId;
    }
}