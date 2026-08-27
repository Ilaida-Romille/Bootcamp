package com.pointwest.bootcamp.eventhubri.modules.agenda.entity;

import com.pointwest.bootcamp.eventhubri.core.model.BaseAuditableEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "speakers")
public class Speaker extends BaseAuditableEntity {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", nullable = false, length = 255)
    private String fullName;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Column(name = "organization_or_title", length = 255)
    private String organizationOrTitle;

    @Column(name = "photo_url", length = 500)
    private String photoUrl;
}
