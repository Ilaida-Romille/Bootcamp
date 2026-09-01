package com.pointwest.bootcamp.eventhubri.modules.account.repository;

import java.util.Collection;
import java.util.List;

import com.pointwest.bootcamp.eventhubri.modules.account.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationRepository
        extends JpaRepository<Organization, Long> {

    boolean existsByPrimaryContactEmailIgnoreCase(
            String primaryContactEmail);

    List<Organization> findByStatus(Organization.Status status);

    List<Organization> findByStatusIn(Collection<Organization.Status> statuses);

    List<Organization> findByStatusNot(Organization.Status status);
}