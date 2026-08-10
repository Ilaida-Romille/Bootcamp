/**
 * Single Responsibility Principle (SRP):
 * Represents an event organizer with admin operations (create, view, edit, delete).
 */
public class Organizer {
    private String organizerId;
    public String name;
    public String email;

    public Organizer(String organizerId, String name, String email) {
        this.organizerId = organizerId;
        this.name = name;
        this.email = email;
    }

    public void createEvent(EventRepository repository, Event event) {
        System.out.println("[Organizer: " + name + "] Creating event: " + event.title);
        repository.saveEvent(event);
    }

    public Event viewEvent(EventRepository repository, String eventId) {
        System.out.println("[Organizer: " + name + "] Viewing event: " + eventId);
        return repository.getEvent(eventId);
    }

    public void editEvent(EventRepository repository, String eventId, String newTitle) {
        System.out.println("[Organizer: " + name + "] Editing event title for ID: " + eventId);
        Event event = repository.getEvent(eventId);
        if (event != null) {
            event.title = newTitle;
            repository.saveEvent(event);
        }
    }

    public void deleteEvent(EventRepository repository, String eventId) {
        System.out.println("[Organizer: " + name + "] Deleting event ID: " + eventId);
        repository.deleteEvent(eventId);
    }

    public String getOrganizerId() {
        return organizerId;
    }
}