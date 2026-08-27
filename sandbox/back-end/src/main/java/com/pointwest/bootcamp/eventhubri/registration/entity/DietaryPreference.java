package com.pointwest.bootcamp.eventhubri.registration.entity;

import com.pointwest.bootcamp.eventhubri.common.entity.BaseEntity;
import com.pointwest.bootcamp.eventhubri.registration.enums.DietaryRestriction;

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

// 1:1 with Registration (ERD: registration_id is itself unique). LAZY so a
// plain registration lookup doesn't force-join the dietary row too.
@Entity
@Table(name = "dietary_preferences")
@Getter @Setter
@NoArgsConstructor
public class DietaryPreference extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "registration_id", nullable = false, unique = true)
    private Registration registration;

    @Enumerated(EnumType.STRING)
    @Column(name = "dietary_restriction", nullable = false, length = 50)
    private DietaryRestriction dietaryRestriction;

    @Column(name = "allergy_notes", columnDefinition = "TEXT")
    private String allergyNotes;
}
