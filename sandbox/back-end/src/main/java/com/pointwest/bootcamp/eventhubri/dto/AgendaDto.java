package com.pointwest.bootcamp.eventhubri.dto;

import com.pointwest.bootcamp.eventhubri.model.Agenda;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AgendaDto {
    private String agendaId;
    private String description;
    private List<SessionDto> sessions = new ArrayList<>();

    public AgendaDto() {
    }

    public AgendaDto(Agenda agenda) {
        if (agenda != null) {
            this.agendaId = agenda.getAgendaId();
            this.description = agenda.getDescription();
            if (agenda.getSessions() != null) {
                this.sessions = agenda.getSessions().stream()
                        .map(SessionDto::new)
                        .collect(Collectors.toList());
            }
        }
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

    public List<SessionDto> getSessions() {
        return sessions;
    }

    public void setSessions(List<SessionDto> sessions) {
        this.sessions = sessions;
    }
}