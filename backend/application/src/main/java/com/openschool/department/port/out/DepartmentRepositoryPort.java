package com.openschool.department.port.out;

import com.openschool.domain.department.Department;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DepartmentRepositoryPort {

    Department save(Department department);
    Optional<Department> findById(Object id);
    Optional<Department> findByName(String name);
    boolean delete(Department department);
    Department update(Department department);
    List<Department> findAll();
}
