package com.pointwest.bootcamp.eventhubri.modules.agenda.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Explicit 3NF junction entity linking an attendee's registration to the
 * sessions they selected. The registration side is a plain scalar FK — the
 * EventRegistration entity belongs to a registration module outside the
 * current scope, so this deliberately does not model that relationship.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Entity
@Table(name = "attendee_sessions")
public class AttendeeSession {

    @EqualsAndHashCode.Include
    @EmbeddedId
    private AttendeeSessionId id;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("sessionId")
    @JoinColumn(name = "session_id", nullable = false)
    private Session session;
}
