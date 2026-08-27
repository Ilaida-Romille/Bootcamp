package com.pointwest.bootcamp.eventhubri.modules.event.entity;

import java.time.LocalDateTime;

import com.pointwest.bootcamp.eventhubri.core.model.BaseAuditableEntity;
import com.pointwest.bootcamp.eventhubri.modules.account.entity.AppUser;
import com.pointwest.bootcamp.eventhubri.modules.account.entity.Organization;

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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(callSuper = true)
@Entity
@Table(name = "events", indexes = {
        @Index(name = "idx_event_organization_id", columnList = "organization_id"),
        @Index(name = "idx_event_created_by_user_id", columnList = "created_by_user_id"),
        @Index(name = "idx_event_start_time", columnList = "start_time")
})
public class Event extends BaseAuditableEntity {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "created_by_user_id", nullable = false)
    private AppUser createdBy;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "banner_image_url", length = 500)
    private String bannerImageUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false, length = 20)
    private EventType eventType;

    // Required when eventType is PHYSICAL or HYBRID — enforced at the service
    // layer.
    @Column(name = "location_address", length = 500)
    private String locationAddress;

    // Required when eventType is VIRTUAL or HYBRID — enforced at the service layer.
    @Column(name = "virtual_meeting_url", length = 500)
    private String virtualMeetingUrl;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Column(name = "is_private", nullable = false)
    private boolean isPrivate;

    @Column(name = "catering_provided", nullable = false)
    private boolean cateringProvided;

    @Column(name = "max_capacity")
    private Integer maxCapacity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Status status;

    public enum EventType {
        PHYSICAL, VIRTUAL, HYBRID
    }

    public enum Status {
        DRAFT, PUBLISHED, COMPLETED, CANCELLED
    }
}
