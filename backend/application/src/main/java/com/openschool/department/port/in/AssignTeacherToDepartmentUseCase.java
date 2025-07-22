package com.openschool.department.port.in;

import com.openschool.department.port.in.command.CreatedDepartmentCommand;
import com.openschool.domain.department.Department;

public interface AssignTeacherToDepartmentUseCase {
    Department createDepartment(CreatedDepartmentCommand createDepartmentCommand);
}
