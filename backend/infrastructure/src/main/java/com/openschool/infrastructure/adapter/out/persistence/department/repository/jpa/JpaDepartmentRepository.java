package com.openschool.infrastructure.adapter.out.persistence.department.repository.jpa;

import com.openschool.infrastructure.adapter.out.persistence.department.entity.DepartmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaDepartmentRepository extends JpaRepository<DepartmentEntity, Long> {

    // This interface extends JpaRepository to provide CRUD operations for DepartmentEntity.
    // Additional custom query methods can be defined here if needed.
}
