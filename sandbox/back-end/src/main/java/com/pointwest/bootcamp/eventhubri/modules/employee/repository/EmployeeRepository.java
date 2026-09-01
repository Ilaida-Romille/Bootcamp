package com.pointwest.bootcamp.eventhubri.modules.employee.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pointwest.bootcamp.eventhubri.modules.account.entity.AppUser;
import com.pointwest.bootcamp.eventhubri.modules.account.entity.Role;

public interface EmployeeRepository extends JpaRepository<AppUser, Long> {

    List<AppUser> findByOrganizationIdAndRoleOrderByLastNameAscFirstNameAsc(Long organizationId, Role role);

    Optional<AppUser> findByIdAndOrganizationIdAndRole(Long id, Long organizationId, Role role);

    boolean existsByEmailIgnoreCase(String email);
}