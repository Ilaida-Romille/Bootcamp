package com.pointwest.bootcamp.eventhubri.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "organizer")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Organizer extends User{
    
    private String organizerId;
}
