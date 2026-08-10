class Organizer {
    private String organizerId;
    private String name;
    private String email;

    public Organizer(String organizerId, String name, String email) {
        this.organizerId = organizerId;
        this.name = name;
        this.email = email;
    }

    public String getOrganizerId() { return organizerId; }
    public String getName() { return name; }
    public String getEmail() { return email; }

    public void createEvent(EventRepository repository, Event event) {
        repository.addEvent(event);
    }

    public Optional<Event> viewEvent(EventRepository repository, String eventId) {
        return repository.getEvent(eventId);
    }

    public void editEvent(EventRepository repository, String eventId, String newTitle, String newStatus) {
        repository.getEvent(eventId).ifPresent(event -> {
            event.setTitle(newTitle);
            event.setStatus(newStatus);
        });
    }

    public void deleteEvent(EventRepository repository, String eventId) {
        repository.deleteEvent(eventId);
    }
}