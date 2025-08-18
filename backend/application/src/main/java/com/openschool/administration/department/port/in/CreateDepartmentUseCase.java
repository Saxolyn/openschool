package com.openschool.administration.department.port.in;

import com.openschool.administration.department.port.in.command.CreatedDepartmentCommand;
import com.openschool.domain.department.Department;

public interface CreateDepartmentUseCase {
    Department createDepartment(CreatedDepartmentCommand createDepartmentCommand);
}
