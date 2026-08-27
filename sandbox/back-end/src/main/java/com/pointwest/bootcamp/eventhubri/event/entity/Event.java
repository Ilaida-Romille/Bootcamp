package com.pointwest.bootcamp.eventhubri.event.entity;

import java.time.LocalDateTime;

import com.pointwest.bootcamp.eventhubri.common.entity.AuditableEntity;
import com.pointwest.bootcamp.eventhubri.event.enums.EventStatus;
import com.pointwest.bootcamp.eventhubri.event.enums.EventType;
import com.pointwest.bootcamp.eventhubri.event.enums.EventVisibility;
import com.pointwest.bootcamp.eventhubri.identity.entity.Organizer;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * `organizer` is LAZY: listing/browsing events (the hottest read path in the
 * app) must not eagerly join organizers. Load the organizer explicitly (or
 * via a fetch-join query / projection) only on the single-event detail view.
 * `clonedFromEvent` is a nullable self-reference, also LAZY for the same
 * reason -- most events are not clones, and even when they are, callers
 * rarely need the source event just to read the clone.
 */
@Entity
@Table(name = "events")
@Getter @Setter
@NoArgsConstructor
public class Event extends AuditableEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "organizer_id", nullable = false)
    private Organizer organizer;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false, length = 50)
    private EventType eventType;

    @Enumerated(EnumType.STRING)
    @Column(name = "visibility", nullable = false, length = 50)
    private EventVisibility visibility;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @Column(name = "timezone", length = 50)
    private String timezone;

    @Column(name = "venue_name", length = 255)
    private String venueName;

    @Column(name = "venue_address", length = 255)
    private String venueAddress;

    @Column(name = "virtual_meeting_url", length = 255)
    private String virtualMeetingUrl;

    @Column(name = "catering_enabled", nullable = false)
    private Boolean cateringEnabled = false;

    @Column(name = "networking_enabled", nullable = false)
    private Boolean networkingEnabled = false;

    @Column(name = "expected_capacity")
    private Integer expectedCapacity;

    @Column(name = "max_capacity")
    private Integer maxCapacity;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private EventStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cloned_from_event_id")
    private Event clonedFromEvent;
}
