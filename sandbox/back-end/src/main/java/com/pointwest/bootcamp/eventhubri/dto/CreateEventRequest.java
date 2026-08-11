package com.pointwest.bootcamp.eventhubri.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateEventRequest {

    private String title;
    private String description;
    private String organizerId;
    private String organizerName;
    private String status;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private LocalDateTime registrationOpensAt;
    private LocalDateTime registrationClosesAt;
    private String venue;
    private String capacity;
}