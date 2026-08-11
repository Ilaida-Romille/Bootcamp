package com.pointwest.bootcamp.eventhubri.model;

import java.util.ArrayList;
import java.util.List;

public class Agenda {
    private String agendaId;
    private String description;
    private List<Session> sessions = new ArrayList<>();

    public Agenda() {
    }

    public Agenda(String agendaId, String description) {
        this.agendaId = agendaId;
        this.description = description;
    }

    public String getAgendaId() {
        return agendaId;
    }

    public void setAgendaId(String agendaId) {
        this.agendaId = agendaId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Session> getSessions() {
        return sessions;
    }

    public void setSessions(List<Session> sessions) {
        this.sessions = sessions;
    }
}
