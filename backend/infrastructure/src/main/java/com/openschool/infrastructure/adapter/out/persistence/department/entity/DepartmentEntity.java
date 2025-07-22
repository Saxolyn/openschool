package com.openschool.infrastructure.adapter.out.persistence.department.entity;

import com.openschool.domain.department.Department;
import com.openschool.infrastructure.adapter.out.persistence.constant.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Entity
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "department")
public class DepartmentEntity extends BaseEntity {
    @Id
    private UUID id;

    private String departmentName;
    private String description;
    private String departmentHead;
    private String departmentDeputy;
    private String departmentCode;
    private String departmentEmail;
    private String departmentPhone;
}
