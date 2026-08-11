import java.util.List;
import java.util.Optional;

public interface EventRepository {
    Event save(Event event);
    Optional<Event> findById(String eventId);
    List<Event> findByOrganizerId(String organizerId);
    List<Event> findAll();
    void deleteById(String eventId);
}