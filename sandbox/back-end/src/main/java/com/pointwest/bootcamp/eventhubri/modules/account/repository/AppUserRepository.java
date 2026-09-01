package com.pointwest.bootcamp.eventhubri.modules.account.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import com.pointwest.bootcamp.eventhubri.modules.account.entity.AppUser;
import com.pointwest.bootcamp.eventhubri.modules.account.entity.Organization;
import com.pointwest.bootcamp.eventhubri.modules.account.entity.Role;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByEmail(String email);

    Optional<AppUser> findByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCase(String email);

    Optional<AppUser> findById(Long id);

    @Query("""
            select u
            from AppUser u
            left join u.organization o
            where u.role = :role
              and (o is null or o.status <> :status)
            """)
    Page<AppUser> findByRoleAndOrganization_StatusNot(
            @Param("role") Role role,
            @Param("status") Organization.Status status,
            Pageable pageable);

    @Query("""
            select u
            from AppUser u
            left join u.organization o
            where u.role = :role
              and (o is null or o.status <> :status)
              and (
                  lower(concat(u.firstName, ' ', u.lastName)) like lower(concat('%', :query, '%'))
                  or lower(u.email) like lower(concat('%', :query, '%'))
                  or lower(coalesce(o.companyName, '')) like lower(concat('%', :query, '%'))
                  or lower(coalesce(o.primaryContactEmail, '')) like lower(concat('%', :query, '%'))
              )
            """)
    Page<AppUser> searchOrganizers(
            @Param("role") Role role,
            @Param("status") Organization.Status status,
            @Param("query") String query,
            Pageable pageable);
}
