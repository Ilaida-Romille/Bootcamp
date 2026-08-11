import java.util.List;
import java.util.Optional;

public interface RegistrationRepository {
    Registration save(Registration registration);
    Optional<Registration> findById(String registrationId);
    List<Registration> findByEventId(String eventId);
    List<Registration> findByAttendeeId(String attendeeId);
    boolean existsByAttendeeIdAndEventId(String attendeeId, String eventId);
    long countByEventIdAndStatus(String eventId, RegistrationStatus status);
    void deleteById(String registrationId);
}