package com.openschool.department.port.in;

import com.openschool.domain.department.Department;

import java.util.List;
import java.util.Optional;

public interface GetDetailDepartmentUseCase {
    Optional<Department> getDepartmentList(Object departmentId);
}
