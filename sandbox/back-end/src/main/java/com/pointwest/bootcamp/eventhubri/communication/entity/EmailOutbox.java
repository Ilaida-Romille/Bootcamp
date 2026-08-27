package com.pointwest.bootcamp.eventhubri.communication.entity;

import java.time.LocalDateTime;

import com.pointwest.bootcamp.eventhubri.common.entity.BaseEntity;
import com.pointwest.bootcamp.eventhubri.communication.enums.EmailStatus;
import com.pointwest.bootcamp.eventhubri.communication.enums.RelatedEntityType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Polymorphic pointer (`relatedEntityId`) is stored deliberately WITHOUT a
 * database FK, matching the ER diagram's own note ("no strict FK by design").
 * Modeling it as a plain Long + enum discriminator instead of a JPA
 * relationship is the correct call here: a real @ManyToOne can only ever
 * target one entity type, so a genuinely polymorphic pointer belongs in the
 * service layer's resolution logic, not in the entity mapping.
 */
@Entity
@Table(name = "email_outbox")
@Getter @Setter
@NoArgsConstructor
public class EmailOutbox extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "related_entity_type", nullable = false, length = 50)
    private RelatedEntityType relatedEntityType;

    @Column(name = "related_entity_id")
    private Long relatedEntityId;

    @Column(name = "recipient_email", nullable = false, length = 255)
    private String recipientEmail;

    @Column(name = "subject", nullable = false, length = 255)
    private String subject;

    @Column(name = "body", nullable = false, columnDefinition = "TEXT")
    private String body;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private EmailStatus status;

    @Column(name = "retry_count", nullable = false)
    private Integer retryCount = 0;

    @Column(name = "scheduled_for")
    private LocalDateTime scheduledFor;

    @Column(name = "sent_at")
    private LocalDateTime sentAt;
}
