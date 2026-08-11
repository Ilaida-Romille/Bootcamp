package com.pointwest.bootcamp.eventhubri.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessionResponse {

    private String agendaItemId;
    private String sessionType; // "PRESENTATION" or "BREAK"
    private String title;
    private String description;
    private String location;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    
    // Subtype-specific fields
    private String speaker;   // Present only if PRESENTATION
    private String breakType; // Present only if BREAK
}