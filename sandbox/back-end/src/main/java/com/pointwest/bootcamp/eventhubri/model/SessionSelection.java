package com.pointwest.bootcamp.eventhubri.model;

import java.util.Date;
import jakarta.persistence.*;

@Entity
@Table(name = "session_selections")
public class SessionSelection {

    @Id
    private String selectionId;

    @Temporal(TemporalType.TIMESTAMP)
    private Date selectedAt;

    @ManyToOne
    @JoinColumn(name = "session_id")
    private Session session;

    public SessionSelection() {
    }

    public SessionSelection(String selectionId, Date selectedAt, Session session) {
        this.selectionId = selectionId;
        this.selectedAt = selectedAt;
        this.session = session;
    }

    public String getSelectionId() {
        return selectionId;
    }

    public void setSelectionId(String selectionId) {
        this.selectionId = selectionId;
    }

    public Date getSelectedAt() {
        return selectedAt;
    }

    public void setSelectedAt(Date selectedAt) {
        this.selectedAt = selectedAt;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }
}
