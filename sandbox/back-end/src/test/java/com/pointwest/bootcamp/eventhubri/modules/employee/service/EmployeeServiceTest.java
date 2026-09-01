package com.pointwest.bootcamp.eventhubri.modules.employee.service;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pointwest.bootcamp.eventhubri.modules.account.entity.AppUser;
import com.pointwest.bootcamp.eventhubri.modules.account.entity.Organization;
import com.pointwest.bootcamp.eventhubri.modules.account.entity.Role;
import com.pointwest.bootcamp.eventhubri.modules.auth.repository.RefreshTokenRepository;
import com.pointwest.bootcamp.eventhubri.modules.employee.repository.EmployeeRepository;
import com.pointwest.bootcamp.eventhubri.modules.registration.repository.RegistrationRepository;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private RegistrationRepository registrationRepository;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @InjectMocks
    private EmployeeService employeeService;

    private AppUser employee;

    @BeforeEach
    void setUp() {
        Organization organization = new Organization();
        organization.setId(7L);

        employee = AppUser.builder()
                .id(15L)
                .organization(organization)
                .firstName("Jane")
                .lastName("Doe")
                .email("jane@example.com")
                .role(Role.ORGANIZER_STAFF)
                .build();
    }

    @Test
    void deleteEmployee_shouldRemoveDependentRegistrationsAndRefreshTokensBeforeDeletingUser() {
        when(employeeRepository.findByIdAndOrganizationIdAndRole(15L, 7L, Role.ORGANIZER_STAFF))
                .thenReturn(Optional.of(employee));

        employeeService.deleteEmployee(7L, 15L);

        verify(registrationRepository).deleteByAttendee_Id(15L);
        verify(refreshTokenRepository).deleteByUser_Id(15L);
        verify(employeeRepository).delete(employee);
    }
}
