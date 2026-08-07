import java.util.Date;

/**
 * Liskov Substitution Principle (LSP):
 * Subclass of Session. Can be substituted seamlessly anywhere a Session object is expected.
 */
public class PresentationSession extends Session {
    private String agendaItemId;
    public String speaker;

    public PresentationSession(String agendaItemId, String title, String description, Date startDateTime, Date endDateTime, String location, String speaker) {
        super(agendaItemId, title, description, startDateTime, endDateTime, location);
        this.agendaItemId = agendaItemId;
        this.speaker = speaker;
    }

    @Override
    public void displayInfo() {
        System.out.println("[Presentation] " + title + " | Speaker: " + speaker + " | Location: " + location);
    }
}