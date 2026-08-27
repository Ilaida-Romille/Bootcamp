package com.pointwest.bootcamp.eventhubri.networking.entity;

import com.pointwest.bootcamp.eventhubri.common.entity.BaseEntity;
import com.pointwest.bootcamp.eventhubri.identity.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "digital_business_cards")
@Getter @Setter
@NoArgsConstructor
public class DigitalBusinessCard extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "display_name", length = 255)
    private String displayName;

    @Column(name = "job_title", length = 255)
    private String jobTitle;

    @Column(name = "company", length = 255)
    private String company;

    @Column(name = "phone_number", length = 30)
    private String phoneNumber;

    @Column(name = "email", length = 255)
    private String email;

    @Column(name = "linkedin_url", length = 255)
    private String linkedinUrl;

    @Column(name = "is_directory_opt_in", nullable = false)
    private Boolean isDirectoryOptIn = false;
}
