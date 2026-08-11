package com.pointwest.bootcamp.eventhubri.model;

import jakarta.persistence.*;
import lombok.*;

@MappedSuperclass
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public abstract class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
}
