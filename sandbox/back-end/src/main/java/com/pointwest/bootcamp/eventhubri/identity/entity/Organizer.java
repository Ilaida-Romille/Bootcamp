package com.pointwest.bootcamp.eventhubri.identity.entity;

import java.time.LocalDateTime;

import com.pointwest.bootcamp.eventhubri.common.entity.AuditableEntity;
import com.pointwest.bootcamp.eventhubri.identity.enums.OrganizerStatus;

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
 * FIX: the ER diagram's `organizers.approved_by` FK (-> the platform-owner
 * user who approved the org) had no corresponding field at all. Added it as
 * a LAZY @ManyToOne so listing/browsing organizers never drags in a User row
 * unless the approver is actually needed.
 * Also removed the unused `java.time.LocalDate` import.
 */
@Entity
@Table(name = "organizers")
@Getter @Setter
@NoArgsConstructor
public class Organizer extends AuditableEntity {

    @Column(name = "company_name", length = 255)
    private String companyName;

    @Column(name = "legal_entity_name", length = 255)
    private String legalEntityName;

    @Column(name = "billing_email", length = 255)
    private String billingEmail;

    @Column(name = "address_line_1", length = 255)
    private String addressLine1;

    @Column(name = "address_line_2", length = 255)
    private String addressLine2;

    @Column(name = "city", length = 100)
    private String city;

    @Column(name = "state_province", length = 100)
    private String stateProvince;

    @Column(name = "postal_code", length = 20)
    private String postalCode;

    @Column(name = "country", length = 100)
    private String country;

    @Enumerated(EnumType.STRING)
    @Column(name = "organizer_status", nullable = false, length = 50)
    private OrganizerStatus accountStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by")
    private User approvedBy;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;
}
