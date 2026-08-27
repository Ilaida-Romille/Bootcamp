package com.pointwest.bootcamp.eventhubri.modules.agenda.entity;

import java.time.LocalDate;

import com.pointwest.bootcamp.eventhubri.core.model.BaseAuditableEntity;
import com.pointwest.bootcamp.eventhubri.modules.event.entity.Event;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "agendas", indexes = {
        @Index(name = "idx_agenda_event_id", columnList = "event_id"),
        @Index(name = "idx_agenda_date", columnList = "agenda_date")
})
public class Agenda extends BaseAuditableEntity {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Column(name = "agenda_date", nullable = false)
    private LocalDate agendaDate;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(length = 1000)
    private String description;
}
