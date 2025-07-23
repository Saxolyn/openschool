package com.openschool.infrastructure.adapter.out.persistence.department.repository;

import com.openschool.department.port.out.DepartmentRepositoryPort;
import com.openschool.domain.department.Department;
import com.openschool.infrastructure.adapter.out.persistence.department.repository.jpa.JpaDepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DepartmentRepositoryAdapter implements DepartmentRepositoryPort {

    private final JpaDepartmentRepository jpaDepartmentRepository;

    @Override
    public Department save(Department department) {
        return null;
    }

    @Override
    public Optional<Department> findById(Object id) {
        return Optional.empty();
    }

    @Override
    public Optional<Department> findByName(String name) {
        return Optional.empty();
    }

    @Override
    public boolean delete(Department department) {
return false;
    }

    @Override
    public Department update(Department department) {
        return null;
    }

    @Override
    public List<Department> findAll() {
        return List.of();
    }

}
