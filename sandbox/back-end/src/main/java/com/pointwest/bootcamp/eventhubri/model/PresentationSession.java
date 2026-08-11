package com.pointwest.bootcamp.eventhubri.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;

@Entity
@DiscriminatorValue("PRESENTATION")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class PresentationSession extends Session {

    private String speaker;
}