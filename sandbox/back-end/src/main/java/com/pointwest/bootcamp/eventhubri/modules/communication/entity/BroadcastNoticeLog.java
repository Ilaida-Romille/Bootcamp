package com.pointwest.bootcamp.eventhubri.modules.communication.entity;

import java.time.LocalDateTime;

import com.pointwest.bootcamp.eventhubri.core.model.BaseAuditableEntity;
import com.pointwest.bootcamp.eventhubri.modules.account.entity.AppUser;
import com.pointwest.bootcamp.eventhubri.modules.event.entity.Event;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * A single announcement broadcast to every registrant of an event. Unlike
 * {@link EmailNotificationLog} there is no per-recipient column — one row
 * represents the whole broadcast, not one delivery.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(callSuper = true)
@Entity
@Table(name = "broadcast_notice_logs", indexes = {
        @Index(name = "idx_broadcast_log_event_id", columnList = "event_id"),
        @Index(name = "idx_broadcast_log_sender_user_id", columnList = "sender_user_id"),
        @Index(name = "idx_broadcast_log_sent_at", columnList = "sent_at")
})
public class BroadcastNoticeLog extends BaseAuditableEntity {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sender_user_id", nullable = false)
    private AppUser sender;

    @Column(nullable = false, length = 255)
    private String subject;

    @Column(name = "message_body", nullable = false, columnDefinition = "TEXT")
    private String messageBody;

    @Column(name = "sent_at", nullable = false)
    private LocalDateTime sentAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_status", nullable = false, length = 20)
    private DeliveryStatus deliveryStatus;
}
