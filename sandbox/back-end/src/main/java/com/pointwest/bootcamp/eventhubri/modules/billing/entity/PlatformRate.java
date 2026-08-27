package com.pointwest.bootcamp.eventhubri.modules.billing.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.pointwest.bootcamp.eventhubri.core.model.BaseAuditableEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
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
@Table(name = "platform_rates", indexes = {
        @Index(name = "idx_rate_effective_start_date", columnList = "effective_start_date"),
        @Index(name = "idx_rate_is_active", columnList = "is_active")
})
public class PlatformRate extends BaseAuditableEntity {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rate_per_attendee", nullable = false, precision = 10, scale = 2)
    private BigDecimal ratePerAttendee;

    @Column(name = "effective_start_date", nullable = false)
    private LocalDate effectiveStartDate;

    @Column(name = "effective_end_date")
    private LocalDate effectiveEndDate;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;
}
