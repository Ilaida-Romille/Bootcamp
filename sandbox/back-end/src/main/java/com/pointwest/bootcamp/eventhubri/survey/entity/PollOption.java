package com.pointwest.bootcamp.eventhubri.survey.entity;

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
@Table(name = "poll_options")
@Getter @Setter
@NoArgsConstructor
public class PollOption extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "live_poll_id", nullable = false)
    private LivePoll livePoll;

    @Column(name = "option_text", nullable = false, length = 255)
    private String optionText;

    @Column(name = "display_order")
    private Integer displayOrder;
}
