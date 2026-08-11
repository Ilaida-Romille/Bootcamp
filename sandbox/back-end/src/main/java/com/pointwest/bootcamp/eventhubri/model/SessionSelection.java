import java.time.LocalDateTime;

public class SessionSelection {
    private String selectionId;
    private Session session;
    private LocalDateTime selectedAt;

    public SessionSelection() {
        this.selectedAt = LocalDateTime.now();
    }

    public SessionSelection(String selectionId, Session session) {
        this.selectionId = selectionId;
        this.session = session;
        this.selectedAt = LocalDateTime.now();
    }

    public String getSelectionId() {
        return selectionId;
    }

    public void setSelectionId(String selectionId) {
        this.selectionId = selectionId;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public LocalDateTime getSelectedAt() {
        return selectedAt;
    }

    public void setSelectedAt(LocalDateTime selectedAt) {
        this.selectedAt = selectedAt;
    }
}