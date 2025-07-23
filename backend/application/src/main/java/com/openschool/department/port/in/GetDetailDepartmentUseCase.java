package com.openschool.department.port.in;

import com.openschool.domain.department.Department;

import java.util.Optional;

public interface GetDetailDepartmentUseCase {
    Department getDetailDepartment(Object departmentId);
}
