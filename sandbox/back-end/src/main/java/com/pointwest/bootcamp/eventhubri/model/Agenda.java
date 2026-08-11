package com.pointwest.bootcamp.eventhubri.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "agendas")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Agenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "agenda_id") // Creates foreign key in sessions table
    private List<Session> sessions = new ArrayList<>();
}