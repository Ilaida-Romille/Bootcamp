package com.pointwest.bootcamp.eventhubri.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "attendees")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Attendee extends User{
    
    private String attendeeId;
}
