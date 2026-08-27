package com.pointwest.bootcamp.eventhubri.billing.entity;

import java.math.BigDecimal;

import com.pointwest.bootcamp.eventhubri.common.entity.BaseEntity;
import com.pointwest.bootcamp.eventhubri.event.entity.Event;

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
@Table(name = "invoice_items")
@Getter @Setter
@NoArgsConstructor
public class InvoiceItem extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "invoice_id", nullable = false)
    private Invoice invoice;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    // Snapshot column, deliberately duplicating Event.title at billing time --
    // this is intentional denormalization for audit integrity: if the event
    // title is edited (or the event is later deleted), the invoice must keep
    // showing what the customer was actually billed for.
    @Column(name = "event_title_snapshot", nullable = false, length = 255)
    private String eventTitleSnapshot;

    @Column(name = "attendee_count", nullable = false)
    private Integer attendeeCount;

    @Column(name = "rate_applied", nullable = false, precision = 10, scale = 2)
    private BigDecimal rateApplied;

    @Column(name = "line_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal lineTotal;
}
