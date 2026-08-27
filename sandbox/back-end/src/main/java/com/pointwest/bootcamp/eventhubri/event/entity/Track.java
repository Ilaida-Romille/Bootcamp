package com.pointwest.bootcamp.eventhubri.event.entity;

import com.pointwest.bootcamp.eventhubri.common.entity.BaseEntity;

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
@Table(name = "tracks")
@Getter @Setter
@NoArgsConstructor
public class Track extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Column(name = "track_name", nullable = false, length = 255)
    private String trackName;

    @Column(name = "track_color", length = 20)
    private String trackColor;

    @Column(name = "description", length = 255)
    private String description;
}
