package com.pointwest.bootcamp.eventhubri.modules.billing.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pointwest.bootcamp.eventhubri.modules.billing.entity.OrganizationInvoice;

public interface OrganizationInvoiceRepository extends JpaRepository<OrganizationInvoice, Long> {

    List<OrganizationInvoice> findByOrganization_Id(Long organizationId);

    List<OrganizationInvoice> findByBillingPeriodStartAndBillingPeriodEnd(
            LocalDate billingPeriodStart, LocalDate billingPeriodEnd);
}