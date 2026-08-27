package com.pointwest.bootcamp.eventhubri.modules.account.repository;

import com.pointwest.bootcamp.eventhubri.modules.account.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationRepository
        extends JpaRepository<Organization, Long> {

    boolean existsByPrimaryContactEmailIgnoreCase(
            String primaryContactEmail);
}