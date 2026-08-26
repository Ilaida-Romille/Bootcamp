package com.pointwest.bootcamp.eventhubri.identity.entity;

import java.time.LocalDateTime;

import com.pointwest.bootcamp.eventhubri.common.entity.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_role")
@Getter @Setter
@NoArgsConstructor
public class UserRole extends BaseEntity{
    
    private User user;
    private Role role;
    private Organizer organizer;
    private LocalDateTime assignedAt = LocalDateTime.now();

    public UserRole(User user, Role role, Organizer organizer, LocalDateTime assignedAt){
        this.user = user;
        this.role = role;
        this.organizer = organizer;
        this.assignedAt = assignedAt;
    }
}
