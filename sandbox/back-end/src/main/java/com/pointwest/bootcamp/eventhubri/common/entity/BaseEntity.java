package com.pointwest.bootcamp.eventhubri.common.entity;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * FIXES APPLIED vs. original:
 * 1. updatedAt was annotated @CreatedDate (bug) -> now @LastModifiedDate so
 *    Spring Data JPA auditing actually refreshes it on every update.
 * 2. updatedAt had updatable = false (bug) -> removed; otherwise Hibernate
 *    would never persist the refreshed timestamp on UPDATE statements.
 * 3. equals()/hashCode() kept as "constant hashCode + id-based equals" -- this
 *    is the recommended Hibernate/JPA entity pattern (stable hashCode across
 *    transient -> persisted lifecycle), not a bug.
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter @Setter
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaseEntity that = (BaseEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
