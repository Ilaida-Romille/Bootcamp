package com.pointwest.bootcamp.eventhubri.registration.entity;

import java.time.LocalDateTime;

import com.pointwest.bootcamp.eventhubri.common.entity.AuditableEntity;
import com.pointwest.bootcamp.eventhubri.event.entity.Event;
import com.pointwest.bootcamp.eventhubri.identity.entity.User;
import com.pointwest.bootcamp.eventhubri.registration.enums.RegistrationStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// event/user are the two most-joined columns from this table (capacity
// checks, "my registrations" screens) -- both LAZY, loaded explicitly with a
// fetch-join/projection in the repository query that actually needs them,
// rather than dragging both parents in on every registration read.
@Entity
@Table(name = "registrations", uniqueConstraints = {
    @UniqueConstraint(name = "uk_event_user_registration", columnNames = {"event_id", "user_id"})
})
@Getter @Setter
@NoArgsConstructor
public class Registration extends AuditableEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "registration_status", nullable = false, length = 50)
    private RegistrationStatus registrationStatus;

    @Column(name = "registered_at")
    private LocalDateTime registeredAt;

    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;
}
