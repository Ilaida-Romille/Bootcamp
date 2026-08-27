package com.pointwest.bootcamp.eventhubri.event.entity;

import com.pointwest.bootcamp.eventhubri.common.entity.BaseEntity;
import com.pointwest.bootcamp.eventhubri.event.enums.SpeakerRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "session_speakers", uniqueConstraints = {
    @UniqueConstraint(name = "uk_session_speaker", columnNames = {"session_id", "speaker_id"})
})
@Getter @Setter
@NoArgsConstructor
public class SessionSpeaker extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "session_id", nullable = false)
    private Session session;

    // EAGER: a session's speaker list is almost always rendered together with
    // the speaker's name/photo, so this avoids an N+1 per row when displaying
    // an agenda -- pair this with an explicit JOIN FETCH from the repository
    // when loading many SessionSpeakers at once.
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "speaker_id", nullable = false)
    private Speaker speaker;

    @Enumerated(EnumType.STRING)
    @Column(name = "speaker_role", nullable = false, length = 50)
    private SpeakerRole speakerRole;
}
