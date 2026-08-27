package com.pointwest.bootcamp.eventhubri.modules.networking.entity;

import com.pointwest.bootcamp.eventhubri.core.model.BaseAuditableEntity;
import com.pointwest.bootcamp.eventhubri.modules.account.entity.AppUser;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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
@Table(name = "digital_business_cards")
public class DigitalBusinessCard extends BaseAuditableEntity {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ToString.Exclude
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private AppUser user;

    @Column(name = "user_profile_picture_url", length = 500)
    private String userProfilePictureUrl;

    @Column(length = 255)
    private String headline;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Column(name = "linkedin_profile_url", length = 500)
    private String linkedinProfileUrl;

    @Column(name = "phone_number", length = 30)
    private String phoneNumber;

    @Column(name = "company_name", length = 255)
    private String companyName;

    @Column(name = "job_title", length = 255)
    private String jobTitle;
}
