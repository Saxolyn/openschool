package com.openschool.administration.department.port.in;

import com.openschool.administration.department.port.in.command.UpdateDepartmentCommand;
import com.openschool.domain.department.Department;

public interface UpdateDepartmentUseCase {
    Department updateDepartment(UpdateDepartmentCommand updateDepartmentCommand);
}
