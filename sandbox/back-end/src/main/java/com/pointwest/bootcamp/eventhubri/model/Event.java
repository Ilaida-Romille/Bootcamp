package com.pointwest.bootcamp.eventhubri.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String eventId;
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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "agenda_id", referencedColumnName = "id")
    private Agenda agenda;
}