package com.openschool.department.service;

import com.openschool.department.exception.DepartmentException;
import com.openschool.department.exception.ExceptionMessage;
import com.openschool.department.port.in.*;
import com.openschool.department.port.in.command.CreatedDepartmentCommand;
import com.openschool.department.port.in.command.UpdateDepartmentCommand;
import com.openschool.department.port.out.DepartmentRepositoryPort;
import com.openschool.domain.department.Department;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Optional;

import static com.openschool.department.mapper.departmentMapper.toDepartment;
import static com.openschool.department.mapper.departmentMapper.toUpdateDepartment;

@AllArgsConstructor
public class DepartmentService implements CreateDepartmentUseCase,
        UpdateDepartmentUseCase,
        DeleteDepartmentUseCase,
        GetListDepartmentUseCase,
        GetDetailDepartmentUseCase {

    private DepartmentRepositoryPort departmentRepositoryPort;

    @Override
    public Department createDepartment(CreatedDepartmentCommand createDepartmentCommand) {
        departmentRepositoryPort.findByName(createDepartmentCommand.getDepartmentName())
                .orElseThrow(() -> new DepartmentException(ExceptionMessage.DEPARTMENT_ALREADY_EXISTS));
        return toDepartment(createDepartmentCommand);
    }

    @Override
    public Department updateDepartment(UpdateDepartmentCommand updateDepartmentCommand) {
        return departmentRepositoryPort.findById(updateDepartmentCommand.getDepartmentId())
                .map(department ->
                        toUpdateDepartment(updateDepartmentCommand, department))
                .orElseThrow(() -> new DepartmentException(ExceptionMessage.DEPARTMENT_NOT_FOUND));
    }

    @Override
    public void deleteDepartment(Object id) {
        Department department = departmentRepositoryPort.findById(id).orElseThrow(() -> new DepartmentException(ExceptionMessage.DEPARTMENT_NOT_FOUND));
        boolean result = departmentRepositoryPort.delete(department);
        if (!result) {
            throw new DepartmentException(ExceptionMessage.DEPARTMENT_DELETION_FAIL);
        }
    }

    @Override
    public List<Department> getDepartmentList() {
        return departmentRepositoryPort.findAll();
    }

    @Override
    public Optional<Department> getDepartmentList(Object departmentId) {
        Optional<Department> department = departmentRepositoryPort.findById(departmentId);
        if (department.isPresent()) {
            throw new DepartmentException(ExceptionMessage.DEPARTMENT_NOT_FOUND);
        }
        return department;
    }
}
