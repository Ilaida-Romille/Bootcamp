package com.pointwest.bootcamp.eventhubri.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;

@Entity
@DiscriminatorValue("BREAK")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class BreakSession extends Session {

    private String breakType;
}