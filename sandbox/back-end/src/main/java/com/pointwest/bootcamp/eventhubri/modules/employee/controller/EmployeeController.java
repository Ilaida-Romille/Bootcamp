package com.pointwest.bootcamp.eventhubri.modules.employee.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pointwest.bootcamp.eventhubri.modules.auth.security.CurrentUser;
import com.pointwest.bootcamp.eventhubri.modules.auth.security.SecurityUser;
import com.pointwest.bootcamp.eventhubri.modules.employee.dto.EmployeeResponseDto;
import com.pointwest.bootcamp.eventhubri.modules.employee.dto.UpdateEmployeeRequestDto;
import com.pointwest.bootcamp.eventhubri.modules.employee.service.EmployeeService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping
    @PreAuthorize("hasAuthority('MANAGE_STAFF')")
    public ResponseEntity<List<EmployeeResponseDto>> getEmployees(@CurrentUser SecurityUser user) {
        return ResponseEntity.ok(employeeService.getEmployees(user.organizationId()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('MANAGE_STAFF')")
    public ResponseEntity<EmployeeResponseDto> getEmployee(
            @CurrentUser SecurityUser user,
            @PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getEmployee(user.organizationId(), id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('MANAGE_STAFF')")
    public ResponseEntity<EmployeeResponseDto> updateEmployee(
            @CurrentUser SecurityUser user,
            @PathVariable Long id,
            @Valid @RequestBody UpdateEmployeeRequestDto dto) {
        return ResponseEntity.ok(employeeService.updateEmployee(user.organizationId(), id, dto));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('MANAGE_STAFF')")
    public ResponseEntity<EmployeeResponseDto> patchEmployee(
            @CurrentUser SecurityUser user,
            @PathVariable Long id,
            @Valid @RequestBody UpdateEmployeeRequestDto dto) {
        return ResponseEntity.ok(employeeService.patchEmployee(user.organizationId(), id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('MANAGE_STAFF')")
    public ResponseEntity<Void> deleteEmployee(
            @CurrentUser SecurityUser user,
            @PathVariable Long id) {
        employeeService.deleteEmployee(user.organizationId(), id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}