package com.pointwest.bootcamp.eventhubri.identity.entity;

import java.time.LocalDateTime;

import com.pointwest.bootcamp.eventhubri.common.entity.AuditableEntity;
import com.pointwest.bootcamp.eventhubri.identity.enums.MembershipStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "organizer_members", uniqueConstraints = {
    @UniqueConstraint(name = "uk_organizer_user", columnNames = {
        "organizer_id", "user_id"
    })
})
@Getter @Setter
@NoArgsConstructor
public class OrganizerMember extends AuditableEntity{
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "organizer_id", nullable = false)
    private Organizer organizer;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private MembershipStatus membershipStatus;

    @Column(name = "joined_at")
    private LocalDateTime joinedAt;

    public OrganizerMember(Organizer organizer, User user, Role role, MembershipStatus membershipStatus, LocalDateTime joinedAt){
        this.organizer = organizer;
        this.user = user;
        this.role = role;
        this.membershipStatus = membershipStatus;
        this.joinedAt = LocalDateTime.now();
    }

}
