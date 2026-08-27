package com.pointwest.bootcamp.eventhubri.survey.entity;

import java.time.LocalDateTime;

import com.pointwest.bootcamp.eventhubri.common.entity.BaseEntity;
import com.pointwest.bootcamp.eventhubri.event.entity.Session;

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
@Table(name = "live_polls")
@Getter @Setter
@NoArgsConstructor
public class LivePoll extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "session_id", nullable = false)
    private Session session;

    @Column(name = "question_text", nullable = false, length = 255)
    private String questionText;

    @Column(name = "is_open", nullable = false)
    private Boolean isOpen = true;

    @Column(name = "opened_at")
    private LocalDateTime openedAt;

    @Column(name = "closed_at")
    private LocalDateTime closedAt;
}
