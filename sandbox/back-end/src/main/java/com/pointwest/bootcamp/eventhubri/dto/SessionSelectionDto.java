package com.pointwest.bootcamp.eventhubri.dto;

import com.pointwest.bootcamp.eventhubri.model.SessionSelection;
import java.util.Date;

public class SessionSelectionDto {
    private String selectionId;
    private Date selectedAt;
    private SessionDto session;

    public SessionSelectionDto() {
    }

    public SessionSelectionDto(SessionSelection selection) {
        if (selection != null) {
            this.selectionId = selection.getSelectionId();
            this.selectedAt = selection.getSelectedAt();
            if (selection.getSession() != null) {
                this.session = new SessionDto(selection.getSession());
            }
        }
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

    public SessionDto getSession() {
        return session;
    }

    public void setSession(SessionDto session) {
        this.session = session;
    }
}
