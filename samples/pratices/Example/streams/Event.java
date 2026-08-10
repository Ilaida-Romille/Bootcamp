import java.time.LocalDateTime;
// ==========================================
// 3. EVENT
// ==========================================
class Event {
    private String eventId;
    private String title;
    private String description;
    private String organizerId;
    private String organizerName;
    private String status; // "DRAFT", "PUBLISHED", "CANCELLED"
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private LocalDateTime registrationOpensAt;
    private LocalDateTime registrationClosesAt;
    private String venue;
    private String bannerImageUrl;
    private int capacity;
    private Agenda agenda;

    public Event(String eventId, String title, String description, String status, LocalDateTime startDateTime, LocalDateTime endDateTime, String venue, int capacity) {
        this.eventId = eventId;
        this.title = title;
        this.description = description;
        this.status = status;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.venue = venue;
        this.capacity = capacity;
    }

    // Getters and Setters
    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getOrganizerId() { return organizerId; }
    public void setOrganizerId(String organizerId) { this.organizerId = organizerId; }

    public String getOrganizerName() { return organizerName; }
    public void setOrganizerName(String organizerName) { this.organizerName = organizerName; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getStartDateTime() { return startDateTime; }
    public void setStartDateTime(LocalDateTime startDateTime) { this.startDateTime = startDateTime; }

    public LocalDateTime getEndDateTime() { return endDateTime; }
    public void setEndDateTime(LocalDateTime endDateTime) { this.endDateTime = endDateTime; }

    public LocalDateTime getRegistrationOpensAt() { return registrationOpensAt; }
    public void setRegistrationOpensAt(LocalDateTime registrationOpensAt) { this.registrationOpensAt = registrationOpensAt; }

    public LocalDateTime getRegistrationClosesAt() { return registrationClosesAt; }
    public void setRegistrationClosesAt(LocalDateTime registrationClosesAt) { this.registrationClosesAt = registrationClosesAt; }

    public String getVenue() { return venue; }
    public void setVenue(String venue) { this.venue = venue; }

    public String getBannerImageUrl() { return bannerImageUrl; }
    public void setBannerImageUrl(String bannerImageUrl) { this.bannerImageUrl = bannerImageUrl; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public Agenda getAgenda() { return agenda; }
    public void setAgenda(Agenda agenda) { this.agenda = agenda; }

    @Override
    public String toString() {
        return String.format("Event[ID=%s, Title='%s', Status='%s', Venue='%s']", eventId, title, status, venue);
    }
}