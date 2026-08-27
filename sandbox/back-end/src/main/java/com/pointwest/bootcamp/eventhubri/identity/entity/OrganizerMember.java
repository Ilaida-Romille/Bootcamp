package com.pointwest.bootcamp.eventhubri.identity.entity;

import java.time.LocalDateTime;

import com.pointwest.bootcamp.eventhubri.common.entity.AuditableEntity;
import com.pointwest.bootcamp.eventhubri.identity.enums.MembershipStatus;

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

/**
 * FIXES APPLIED vs. original:
 * 1. Constructor bug: `this.joinedAt = LocalDateTime.now();` silently ignored
 *    the `joinedAt` parameter the caller passed in -- an invite processed
 *    today could never be back-dated/corrected, and it made the parameter
 *    dead code. Now assigns the parameter as given.
 * 2. Added the ERD's `invited_by` (nullable FK to the inviting User) and
 *    `invited_at` columns, which were missing entirely.
 * 3. Kept the normalized `role` (@ManyToOne Role) instead of the ERD's raw
 *    `member_role` varchar -- reusing the existing Role/RoleType lookup table
 *    avoids a second, parallel enum for "OWNER/TEAM_MEMBER" and keeps role
 *    data in one normalized place (fewer sources of truth = easier to keep
 *    consistent, in the spirit of SRP/DRY).
 */
@Entity
@Table(name = "organizer_members", uniqueConstraints = {
    @UniqueConstraint(name = "uk_organizer_user", columnNames = {
        "organizer_id", "user_id"
    })
})
@Getter @Setter
@NoArgsConstructor
public class OrganizerMember extends AuditableEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "organizer_id", nullable = false)
    private Organizer organizer;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private MembershipStatus membershipStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invited_by")
    private User invitedBy;

    @Column(name = "invited_at")
    private LocalDateTime invitedAt;

    @Column(name = "joined_at")
    private LocalDateTime joinedAt;

    public OrganizerMember(Organizer organizer, User user, Role role, MembershipStatus membershipStatus,
                            User invitedBy, LocalDateTime invitedAt, LocalDateTime joinedAt) {
        this.organizer = organizer;
        this.user = user;
        this.role = role;
        this.membershipStatus = membershipStatus;
        this.invitedBy = invitedBy;
        this.invitedAt = invitedAt;
        this.joinedAt = joinedAt;
    }
}
