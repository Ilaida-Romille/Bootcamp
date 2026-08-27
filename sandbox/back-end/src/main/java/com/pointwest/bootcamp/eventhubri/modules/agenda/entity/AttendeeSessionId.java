package com.pointwest.bootcamp.eventhubri.modules.agenda.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class AttendeeSessionId implements Serializable {

    @Column(name = "registration_id")
    private Long registrationId;

    @Column(name = "session_id")
    private Long sessionId;
}
