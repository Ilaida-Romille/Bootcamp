import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
// ==========================================
// 4. EVENT REPOSITORY (Stream Operations)
// ==========================================
class EventRepository {
    private List<Event> events = new ArrayList<>();

    public void addEvent(Event event) {
        this.events.add(event);
    }

    // Stream: Find event by ID using filter + findFirst
    public Optional<Event> getEvent(String eventId) {
        return events.stream()
                .filter(e -> e.getEventId().equalsIgnoreCase(eventId))
                .findFirst();
    }

    // Stream: Filter events by status
    public List<Event> getEventsByStatus(String status) {
        return events.stream()
                .filter(e -> e.getStatus().equalsIgnoreCase(status))
                .collect(Collectors.toList());
    }

    // Stream: Filter events by organizer ID
    public List<Event> getEventsByOrganizer(String organizerId) {
        return events.stream()
                .filter(e -> e.getOrganizerId().equals(organizerId))
                .collect(Collectors.toList());
    }

    // Stream: Find events containing a key presentation topic/title
    public List<Event> findEventsWithSpeaker(String speakerName) {
        return events.stream()
                .filter(e -> e.getAgenda() != null && 
                        e.getAgenda().getSessions().stream()
                         .filter(PresentationSession.class::isInstance)
                         .map(PresentationSession.class::cast)
                         .anyMatch(ps -> ps.getSpeaker().equalsIgnoreCase(speakerName)))
                .collect(Collectors.toList());
    }

    // Stream: Delete event by ID
    public boolean deleteEvent(String eventId) {
        return events.removeIf(e -> e.getEventId().equalsIgnoreCase(eventId));
    }

    public List<Event> getAllEvents() {
        return new ArrayList<>(events);
    }
}