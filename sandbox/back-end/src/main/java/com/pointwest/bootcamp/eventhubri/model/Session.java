package com.pointwest.bootcamp.eventhubri.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "sessions")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "session_type")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public abstract class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String agendaItemId;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private String title;
    private String description;
    private String location;
}