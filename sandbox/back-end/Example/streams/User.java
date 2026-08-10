import java.util.Optional;
// ==========================================
// 5. USER & ORGANIZER
// ==========================================
class User {
    private String attendeeId;
    private String name;
    private String email;

    public User(String attendeeId, String name, String email) {
        this.attendeeId = attendeeId;
        this.name = name;
        this.email = email;
    }

    public String getAttendeeId() { return attendeeId; }
    public String getName() { return name; }
    public String getEmail() { return email; }

    public Optional<Event> viewEvent(EventRepository repository, String eventId) {
        return repository.getEvent(eventId);
    }
}