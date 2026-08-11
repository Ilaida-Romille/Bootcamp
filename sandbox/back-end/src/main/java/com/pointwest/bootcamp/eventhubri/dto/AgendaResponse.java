package com.pointwest.bootcamp.eventhubri.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgendaResponse {

    private String description;
    private List<SessionResponse> sessions;
}