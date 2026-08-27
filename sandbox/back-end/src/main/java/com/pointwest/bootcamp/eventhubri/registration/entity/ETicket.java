package com.pointwest.bootcamp.eventhubri.registration.entity;

import java.time.LocalDateTime;

import com.pointwest.bootcamp.eventhubri.common.entity.BaseEntity;
import com.pointwest.bootcamp.eventhubri.registration.enums.TicketStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "e_tickets")
@Getter @Setter
@NoArgsConstructor
public class ETicket extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "registration_id", nullable = false, unique = true)
    private Registration registration;

    @Column(name = "qr_code_hash", nullable = false, unique = true, length = 255)
    private String qrCodeHash;

    @Enumerated(EnumType.STRING)
    @Column(name = "ticket_status", nullable = false, length = 50)
    private TicketStatus ticketStatus;

    @Column(name = "issued_at")
    private LocalDateTime issuedAt;
}
