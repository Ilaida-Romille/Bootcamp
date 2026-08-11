package com.pointwest.bootcamp.eventhubri.model;

import java.util.Date;

public class SessionSelection {
    private String selectionId;
    private Date selectedAt;
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
