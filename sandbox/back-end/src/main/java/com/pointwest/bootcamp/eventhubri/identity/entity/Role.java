package com.pointwest.bootcamp.eventhubri.identity.entity;

import com.pointwest.bootcamp.eventhubri.common.entity.BaseEntity;
import com.pointwest.bootcamp.eventhubri.identity.enums.RoleType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// No mapping bugs. Matches the "roles" table exactly. No back-collection to
// UserRole/OrganizerMember added on purpose -- same N+1 reasoning as User.
@Entity
@Table(name = "roles")
@Getter @Setter
@NoArgsConstructor
public class Role extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "role_name", nullable = false, unique = true, length = 50)
    private RoleType roleName;

    @Column(name = "description", length = 255)
    private String description;

    public Role(RoleType roleName, String description) {
        this.roleName = roleName;
        this.description = description;
    }
}
