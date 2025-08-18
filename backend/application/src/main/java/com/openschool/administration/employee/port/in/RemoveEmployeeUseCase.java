package com.openschool.administration.employee.port.in;

import java.util.UUID;

public interface RemoveEmployeeUseCase {
    void removeEmployee(UUID departmentId, UUID employeeId);
}
