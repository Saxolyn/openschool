package com.openschool.administration.employee.port.in;

import com.openschool.domain.employee.Employee;
import com.openschool.administration.employee.port.in.command.UpdatedEmployeeCommand;

public interface UpdateEmployeeUseCase {
    Employee updateEmployee(UpdatedEmployeeCommand command);
}
