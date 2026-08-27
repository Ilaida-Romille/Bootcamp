package com.pointwest.bootcamp.eventhubri.audit.entity;

import com.pointwest.bootcamp.eventhubri.common.entity.BaseEntity;
import com.pointwest.bootcamp.eventhubri.identity.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "platform_audit_logs")
@Getter @Setter
@NoArgsConstructor
public class PlatformAuditLog extends BaseEntity {

    // Nullable per ERD: system-triggered actions have no actor.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actor_user_id")
    private User actorUser;

    @Column(name = "action_type", nullable = false, length = 100)
    private String actionType;

    // entity_type/entity_id are a polymorphic pointer by design (same
    // reasoning as EmailOutbox.relatedEntityId) -- not modeled as a JPA
    // relationship on purpose.
    @Column(name = "entity_type", length = 100)
    private String entityType;

    @Column(name = "entity_id")
    private Long entityId;

    @Column(name = "details", columnDefinition = "TEXT")
    private String details;

    @Column(name = "ip_address", length = 50)
    private String ipAddress;
}
