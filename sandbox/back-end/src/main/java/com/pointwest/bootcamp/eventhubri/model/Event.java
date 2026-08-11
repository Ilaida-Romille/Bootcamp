import java.time.LocalDateTime;

public class Event {
    private String eventId;
    private String title;
    private String description;
    private EventStatus status;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private LocalDateTime registrationOpensAt;
    private LocalDateTime registrationClosesAt;
    private String venue;
    private Integer capacity;
    private boolean isFoodProvided;
    private Agenda agenda;
    private String organizerId;

    public Event() {
        this.status = EventStatus.DRAFT;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EventStatus getStatus() {
        return status;
    }

    public void setStatus(EventStatus status) {
        this.status = status;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    public LocalDateTime getRegistrationOpensAt() {
        return registrationOpensAt;
    }

    public void setRegistrationOpensAt(LocalDateTime registrationOpensAt) {
        this.registrationOpensAt = registrationOpensAt;
    }

    public LocalDateTime getRegistrationClosesAt() {
        return registrationClosesAt;
    }

    public void setRegistrationClosesAt(LocalDateTime registrationClosesAt) {
        this.registrationClosesAt = registrationClosesAt;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public boolean isFoodProvided() {
        return isFoodProvided;
    }

    public void setFoodProvided(boolean foodProvided) {
        isFoodProvided = foodProvided;
    }

    public Agenda getAgenda() {
        return agenda;
    }

    public void setAgenda(Agenda agenda) {
        this.agenda = agenda;
    }

    public String getOrganizerId() {
        return organizerId;
    }

    public void setOrganizerId(String organizerId) {
        this.organizerId = organizerId;
    }
}