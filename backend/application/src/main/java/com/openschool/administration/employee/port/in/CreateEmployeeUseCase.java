package com.openschool.administration.employee.port.in;

import com.openschool.domain.employee.Employee;
import com.openschool.administration.employee.port.in.command.CreatedEmployeeCommand;

public interface CreateEmployeeUseCase {
    Employee createEmployee(CreatedEmployeeCommand command);
}
