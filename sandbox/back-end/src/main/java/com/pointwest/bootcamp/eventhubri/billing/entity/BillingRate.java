package com.pointwest.bootcamp.eventhubri.billing.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.pointwest.bootcamp.eventhubri.common.entity.BaseEntity;
import com.pointwest.bootcamp.eventhubri.identity.entity.Organizer;

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
@Table(name = "billing_rates")
@Getter @Setter
@NoArgsConstructor
public class BillingRate extends BaseEntity {

    // Nullable per ERD: NULL organizer = global default rate.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizer_id")
    private Organizer organizer;

    @Column(name = "rate_per_attendee", nullable = false, precision = 10, scale = 2)
    private BigDecimal ratePerAttendee;

    @Column(name = "currency", nullable = false, length = 10)
    private String currency;

    @Column(name = "effective_from", nullable = false)
    private LocalDateTime effectiveFrom;

    @Column(name = "effective_to")
    private LocalDateTime effectiveTo;
}
