package com.pointwest.bootcamp.eventhubri.common.entity;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import com.pointwest.bootcamp.eventhubri.identity.entity.User;

import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

/**
 * No functional bugs found here. Kept FetchType.LAZY on both associations
 * (correct choice -- eagerly loading the "who created/updated this row" User
 * on every single entity fetch across the whole app would be a guaranteed
 * N+1 / over-fetch problem).
 */
@MappedSuperclass
@Getter @Setter
public abstract class AuditableEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", updatable = false)
    @CreatedBy
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by")
    @LastModifiedBy
    private User updatedBy;
}
