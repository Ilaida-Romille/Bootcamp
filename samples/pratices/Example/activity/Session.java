import java.util.Date;

/**
 * Open-Closed Principle (OCP):
 * The abstract Session class is open for extension (e.g., WorkshopSession, KeynoteSession can be added)
 * but closed for modification.
 */
public abstract class Session {
    private String agendaItemId;
    public Date startDateTime;
    public Date endDateTime;
    public String title;
    public String description;
    public String location;

    public Session(String agendaItemId, String title, String description, Date startDateTime, Date endDateTime, String location) {
        this.agendaItemId = agendaItemId;
        this.title = title;
        this.description = description;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.location = location;
    }

    public String getAgendaItemId() {
        return agendaItemId;
    }

    public abstract void displayInfo();
}