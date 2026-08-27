package com.pointwest.bootcamp.eventhubri.event.entity;

import com.pointwest.bootcamp.eventhubri.common.entity.BaseEntity;
import com.pointwest.bootcamp.eventhubri.event.enums.CustomFieldType;

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

@Entity
@Table(name = "event_custom_fields")
@Getter @Setter
@NoArgsConstructor
public class EventCustomField extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Column(name = "field_label", nullable = false, length = 255)
    private String fieldLabel;

    @Enumerated(EnumType.STRING)
    @Column(name = "field_type", nullable = false, length = 50)
    private CustomFieldType fieldType;

    // JSON array of choice options, stored as raw text (mapping/parsing is a
    // service-layer concern, not the entity's -- keeps this class SRP-clean).
    @Column(name = "field_options", columnDefinition = "TEXT")
    private String fieldOptions;

    @Column(name = "is_required", nullable = false)
    private Boolean isRequired = false;

    @Column(name = "display_order")
    private Integer displayOrder;
}
