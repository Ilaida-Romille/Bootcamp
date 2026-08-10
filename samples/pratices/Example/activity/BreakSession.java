import java.util.Date;

/**
 * Liskov Substitution Principle (LSP):
 * Subclass of Session fulfilling the contract of the parent class.
 */
public class BreakSession extends Session {
    private String agendaItemId;
    public String breakType;

    public BreakSession(String agendaItemId, String title, String description, Date startDateTime, Date endDateTime, String location, String breakType) {
        super(agendaItemId, title, description, startDateTime, endDateTime, location);
        this.agendaItemId = agendaItemId;
        this.breakType = breakType;
    }

    @Override
    public void displayInfo() {
        System.out.println("[Break] " + title + " | Type: " + breakType);
    }
}