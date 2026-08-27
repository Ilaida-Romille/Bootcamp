package com.pointwest.bootcamp.eventhubri.identity.entity;

import java.time.LocalDateTime;

import com.pointwest.bootcamp.eventhubri.common.entity.AuditableEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * FIXES APPLIED vs. original:
 * 1. Declared its own createdAt/updatedAt columns that DUPLICATE the ones
 *    already mapped by BaseEntity (via AuditableEntity) -- this is a real bug:
 *    Hibernate would throw a duplicate-column-mapping error at startup.
 *    Removed both fields; the inherited, audited columns are the single
 *    source of truth (also fixes the unused `java.time.LocalDate` import).
 * 2. Added @Table(name = "users") -- "user" is a reserved word in several
 *    SQL dialects (e.g. PostgreSQL), and the ER diagram names the table
 *    "users" explicitly, so relying on Hibernate's default (unquoted "user")
 *    is both wrong against the target schema and fragile across DBs.
 * 3. Deliberately did NOT add back-reference collections here (List<UserRole>,
 *    List<Registration>, etc.). A User can accumulate thousands of
 *    registrations/roles/messages; mapping them as entity collections on
 *    User would invite N+1 queries and unbounded fetches. Those associations
 *    are queried from the "many" side's repository instead
 *    (e.g. UserRoleRepository.findByUser(user)), which keeps User's
 *    responsibility limited to identity data (SRP).
 */
@Entity
@Table(name = "users")
@Getter @Setter
public class User extends AuditableEntity {

    @Column(name = "email", unique = true, nullable = false, length = 255)
    private String email;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Column(name = "first_name", length = 100)
    private String firstName;

    @Column(name = "last_name", length = 100)
    private String lastName;

    @Column(name = "phone_number", length = 30)
    private String phoneNumber;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;
}
