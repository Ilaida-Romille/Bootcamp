package com.pointwest.bootcamp.eventhubri.core.exception.employee;

import com.pointwest.bootcamp.eventhubri.core.exception.EventhubException;
import com.pointwest.bootcamp.eventhubri.core.exception.enums.EventhubErrorCode;

public class EmployeeNotFoundException extends EventhubException {
    public EmployeeNotFoundException(Long employeeId) {
        super(EventhubErrorCode.EMPLOYEE_NOT_FOUND, "Employee not found: " + employeeId);
    }
}
