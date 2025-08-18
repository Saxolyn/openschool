package com.openschool.administration.employee.port.in;

import java.util.UUID;

public interface DeleteEmployeeUseCase {
    void deleteEmployee(UUID employeeId);
}
