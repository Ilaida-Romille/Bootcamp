import java.util.HashMap;
import java.util.Map;

/**
 * Single Responsibility Principle (SRP) & Dependency Inversion Principle (DIP):
 * Manages storage and retrieval of Event objects.
 * Decouples user classes from actual storage/persistence implementations.
 */
public class EventRepository {
    private Map<String, Event> database = new HashMap<>();

    public Event getEvent(String eventId) {
        System.out.println("[EventRepository] Fetching event ID: " + eventId);
        return database.get(eventId);
    }

    public void saveEvent(Event event) {
        database.put(event.getEventId(), event);
        System.out.println("[EventRepository] Event saved: " + event.title + " (ID: " + event.getEventId() + ")");
    }

    public void deleteEvent(String eventId) {
        database.remove(eventId);
        System.out.println("[EventRepository] Deleted event ID: " + eventId);
    }
}