package com.openschool.infrastructure.adapter.out.persistence.department.repository;

import com.openschool.department.port.out.DepartmentRepositoryPort;
import com.openschool.domain.department.Department;
import com.openschool.infrastructure.adapter.out.persistence.department.repository.jpa.JpaDepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

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
    public void deleteById(Object id) {

    }

    @Override
    public Department update(Department department) {
        return null;
    }

    @Override
    public Optional<Department> findByIdentityId(UUID identityId) {
        return Optional.empty();
    }
}
