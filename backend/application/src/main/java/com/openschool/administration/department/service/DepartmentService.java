package com.openschool.administration.department.service;

import com.openschool.administration.department.exception.DepartmentException;
import com.openschool.administration.department.exception.ExceptionMessage;
import com.openschool.administration.department.port.in.*;
import com.openschool.administration.department.port.in.command.CreatedDepartmentCommand;
import com.openschool.administration.department.port.in.command.UpdateDepartmentCommand;
import com.openschool.administration.department.port.out.DepartmentRepositoryPort;
import com.openschool.common.pageable.PageInfo;
import com.openschool.common.pageable.PageResult;
import com.openschool.domain.department.Department;
import lombok.AllArgsConstructor;

import java.util.UUID;

import static com.openschool.administration.department.mapper.DepartmentMapper.toDepartment;
import static com.openschool.administration.department.mapper.DepartmentMapper.toUpdateDepartment;

@AllArgsConstructor
public class DepartmentService implements CreateDepartmentUseCase,
        UpdateDepartmentUseCase,
        DeleteDepartmentUseCase,
        GetListDepartmentUseCase,
        GetDetailDepartmentUseCase {

    private DepartmentRepositoryPort departmentRepositoryPort;

    @Override
    public Department createDepartment(CreatedDepartmentCommand createDepartmentCommand) {
        if (departmentRepositoryPort.findByDepartmentName(createDepartmentCommand.getDepartmentName()).isPresent()) {
            throw new DepartmentException(ExceptionMessage.DEPARTMENT_ALREADY_EXISTS);
        }
        return departmentRepositoryPort.save(toDepartment(createDepartmentCommand));
    }

    @Override
    public Department updateDepartment(UpdateDepartmentCommand updateDepartmentCommand) {
        Department currentDepartment = departmentRepositoryPort.findById(updateDepartmentCommand.getDepartmentId())
                .map(department ->
                        toUpdateDepartment(updateDepartmentCommand, department))
                .orElseThrow(() -> new DepartmentException(ExceptionMessage.DEPARTMENT_NOT_FOUND));
        return departmentRepositoryPort.update(currentDepartment);
    }

    @Override
    public PageResult<Department> getDepartmentList(PageInfo pageInfo) {
        return departmentRepositoryPort.findAll(pageInfo);
    }

    @Override
    public Department getDetailDepartment(UUID departmentId) {
        return departmentRepositoryPort.findById(departmentId)
                .orElseThrow(() -> new DepartmentException(ExceptionMessage.DEPARTMENT_NOT_FOUND));

    }

    @Override
    public void deleteDepartment(UUID departmentId) {
        Department department = departmentRepositoryPort.findById(departmentId).orElseThrow(() -> new DepartmentException(ExceptionMessage.DEPARTMENT_NOT_FOUND));
        boolean result = departmentRepositoryPort.delete(department);
        if (!result) {
            throw new DepartmentException(ExceptionMessage.DEPARTMENT_DELETION_FAIL);
        }
    }

}
