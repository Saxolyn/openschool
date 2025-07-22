package com.openschool.department.port.out;

import com.openschool.domain.department.Department;

import java.util.Optional;
import java.util.UUID;

public interface DepartmentRepositoryPort {

    Department save(Department department);
    Optional<Department> findById(Object id);
    Optional<Department> findByName(String name);
    void deleteById(Object id);
    Department update(Department department);
    Optional<Department> findByIdentityId(UUID identityId);
}
