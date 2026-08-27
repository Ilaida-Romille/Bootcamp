package com.pointwest.bootcamp.eventhubri.identity.entity;

import java.time.LocalDateTime;

import com.pointwest.bootcamp.eventhubri.common.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * FIX (critical bug): the original class declared `user`, `role`, `organizer`
 * as plain, unannotated fields. With no @ManyToOne/@JoinColumn, JPA would
 * either ignore them entirely (fields lost) or fail to map them at all --
 * either way, "who has what role" was never actually being persisted.
 * Added proper @ManyToOne mappings (EAGER for user/role since a UserRole
 * row is meaningless without them and they're almost always needed together
 * with the join row itself -- avoids a second round trip for tiny lookup
 * tables), and LAZY for the nullable, org-scoping `organizer`.
 * Also added the natural uniqueness constraint implied by the ERD
 * (a user shouldn't hold the same role twice in the same org scope) and
 * removed the mutable field initializer `= LocalDateTime.now()`, which is a
 * classic JPA anti-pattern (every fresh instance -- including ones Hibernate
 * builds when hydrating from the DB -- would silently get "now" baked in
 * before the persisted value overwrites it, and it's redundant with the
 * assignedAt argument the explicit constructor already sets).
 */
@Entity
@Table(name = "user_role", uniqueConstraints = {
    @UniqueConstraint(name = "uk_user_role_org", columnNames = {
        "user_id", "role_id", "organizer_id"
    })
})
@Getter @Setter
@NoArgsConstructor
public class UserRole extends BaseEntity {

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizer_id")
    private Organizer organizer;

    @Column(name = "assigned_at", nullable = false)
    private LocalDateTime assignedAt;

    public UserRole(User user, Role role, Organizer organizer, LocalDateTime assignedAt) {
        this.user = user;
        this.role = role;
        this.organizer = organizer;
        this.assignedAt = assignedAt;
    }
}
