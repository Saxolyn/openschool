package com.openschool.department.service;

import com.openschool.department.exception.DepartmentException;
import com.openschool.department.exception.ExceptionMessage;
import com.openschool.department.port.in.CreateDepartmentUseCase;
import com.openschool.department.port.in.DeleteDepartmentUseCase;
import com.openschool.department.port.in.ListDepartmentUseCase;
import com.openschool.department.port.in.UpdateDepartmentUseCase;
import com.openschool.department.port.in.command.CreatedDepartmentCommand;
import com.openschool.department.port.in.command.UpdateDepartmentCommand;
import com.openschool.department.port.out.DepartmentRepositoryPort;
import com.openschool.domain.department.Department;
import lombok.AllArgsConstructor;

import java.util.List;

import static com.openschool.department.mapper.departmentMapper.toDepartment;
import static com.openschool.department.mapper.departmentMapper.toUpdateDepartment;

@AllArgsConstructor
public class DepartmentService implements CreateDepartmentUseCase, UpdateDepartmentUseCase, DeleteDepartmentUseCase, ListDepartmentUseCase {

    private DepartmentRepositoryPort departmentRepositoryPort;

    @Override
    public Department createDepartment(CreatedDepartmentCommand createDepartmentCommand) {
        departmentRepositoryPort.findByName(createDepartmentCommand.getDepartmentName())
                .orElseThrow(()-> new DepartmentException(ExceptionMessage.DEPARTMENT_ALREADY_EXISTS));
        return toDepartment(createDepartmentCommand);
    }

    @Override
    public Department updateDepartment(UpdateDepartmentCommand updateDepartmentCommand) {
        return departmentRepositoryPort.findById(updateDepartmentCommand.getDepartmentId())
                .map( department ->
                            toUpdateDepartment(updateDepartmentCommand, department))
                .orElseThrow(() -> new DepartmentException(ExceptionMessage.DEPARTMENT_NOT_FOUND));
    }

    @Override
    public void deleteDepartment(Long id) {
        // Implementation for deleting a department
    }

    @Override
    public List<Department> getDepartmentList() {
        // Implementation for listing departments
        return null;
    }
}
