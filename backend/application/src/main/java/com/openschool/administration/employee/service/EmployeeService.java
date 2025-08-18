package com.openschool.administration.employee.service;

import com.openschool.administration.department.exception.DepartmentException;
import com.openschool.administration.department.port.out.DepartmentRepositoryPort;
import com.openschool.administration.employee.exception.EmployeeException;
import com.openschool.administration.employee.port.in.*;
import com.openschool.administration.employee.port.in.command.CreatedEmployeeCommand;
import com.openschool.administration.employee.port.in.command.UpdatedEmployeeCommand;
import com.openschool.administration.employee.port.out.EmployeeRepositoryPort;
import com.openschool.common.pageable.PageInfo;
import com.openschool.common.pageable.PageResult;
import com.openschool.domain.department.Department;
import com.openschool.domain.employee.Employee;
import com.openschool.domain.employee.EmployeeType;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static com.openschool.administration.department.exception.ExceptionMessage.DEPARTMENT_NOT_FOUND;
import static com.openschool.administration.employee.exception.ExceptionMessage.*;
import static com.openschool.administration.employee.mapper.EmployeeMapper.toEmployee;

@RequiredArgsConstructor
@Data
public class EmployeeService implements
        CreateEmployeeUseCase,
        UpdateEmployeeUseCase,
        GetDetailEmployeeUseCase,
        DeleteEmployeeUseCase,
        GetListEmployeeUseCase,
        AssignEmployeeUseCase,
        GetListEmployeeInDepartmentUseCase,
        MoveEmployeeUseCase,
        RemoveEmployeeUseCase,
        SetEmployeeTypeUseCase {

    private final EmployeeRepositoryPort employeeRepositoryPort;
    private final DepartmentRepositoryPort departmentRepositoryPort;

    @Override
    public Employee createEmployee(CreatedEmployeeCommand command) {
        Optional<Employee> currentEmployee = employeeRepositoryPort.findByPhoneNumberOrEmail(command.getPhoneNumber(), command.getEmail());
        if (currentEmployee.isPresent()) {
            throw new EmployeeException(EMPLOYEE_ALREADY_EXISTS);
        }
        return employeeRepositoryPort.createEmployee(toEmployee(command))
                .orElseThrow(() -> new EmployeeException(EMPLOYEE_CREATION_FAIL));
    }

    @Override
    public Employee updateEmployee(UpdatedEmployeeCommand command) {
        Employee currentEmployee = employeeRepositoryPort.getDetailEmployee(command.getEmployeeId())
                .orElseThrow(() -> new EmployeeException(EMPLOYEE_NOT_FOUND));
        return employeeRepositoryPort.updateEmployee(toEmployee(command, currentEmployee))
                .orElseThrow(() -> new EmployeeException(EMPLOYEE_UPDATE_FAIL));
    }

    @Override
    public void deleteEmployee(UUID employeeId) {
        Employee currentEmployee = employeeRepositoryPort.getDetailEmployee(employeeId)
                .orElseThrow(() -> new EmployeeException(EMPLOYEE_NOT_FOUND));
        boolean result = employeeRepositoryPort.deleteEmployee(currentEmployee.getEmployeeId());
        if (!result) {
            throw new EmployeeException(EMPLOYEE_DELETION_FAIL);
        }
    }

    @Override
    public Employee getDetailEmployee(UUID employeeId) {
        return employeeRepositoryPort.getDetailEmployee(employeeId)
                .orElseThrow(() -> new EmployeeException(EMPLOYEE_NOT_FOUND));
    }

    @Override
    public PageResult<Employee> getListEmployee(PageInfo pageinfo) {
        return employeeRepositoryPort.getListEmployee(pageinfo);
    }

    @Override
    public void assignEmployee(UUID departmentId, List<UUID> employeeId) {
        Department currentDepartment = departmentRepositoryPort.findById(departmentId)
                .orElseThrow(() -> new DepartmentException(DEPARTMENT_NOT_FOUND));

        List<Employee> currentEmployees = employeeId.stream()
                .map(employeeRepositoryPort::getDetailEmployee)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(employee -> employee.getDepartment().equals(currentDepartment.getDepartmentId()))
                .toList();

        if (currentEmployees.isEmpty()) {
            throw new EmployeeException(EMPLOYEE_ASSIGNED_FAIL);
        }

        boolean result = employeeRepositoryPort.assignEmployeeToDepartment(
                currentDepartment.getDepartmentId(),
                currentEmployees
        );

        if (!result) {
            throw new EmployeeException(EMPLOYEE_ASSIGNED_FAIL);
        }
    }

    @Override
    public PageResult<Employee> getListEmployee(PageInfo pageInfo, UUID departmentId) {
        Department currentDepartment = departmentRepositoryPort.findById(departmentId)
                .orElseThrow(() -> new DepartmentException(DEPARTMENT_NOT_FOUND));
        return employeeRepositoryPort.getListEmployeeInDepartment(pageInfo, currentDepartment.getDepartmentId());
    }

    @Override
    public void moveEmployee(UUID srcDepartmentId, UUID desDepartmentId, UUID employeeId) {
        Employee currentEmployee = employeeRepositoryPort.getDetailEmployee(employeeId)
                .orElseThrow(() -> new EmployeeException(EMPLOYEE_NOT_FOUND));
        Department currentSourceDepartment = departmentRepositoryPort.findById(srcDepartmentId)
                .orElseThrow(() -> new DepartmentException(DEPARTMENT_NOT_FOUND));
        Department currentDestinationDepartment = departmentRepositoryPort.findById(desDepartmentId)
                .orElseThrow(() -> new DepartmentException(DEPARTMENT_NOT_FOUND));
        if (!Objects.equals(currentEmployee.getDepartment(), currentSourceDepartment.getDepartmentId())) {
            throw new EmployeeException(EMPLOYEE_NOT_IN_THIS_DEPARTMENT_YET);
        }

        boolean result = employeeRepositoryPort.moveEmployee(
                currentDestinationDepartment.getDepartmentId(),
                currentEmployee);

        if (!result) {
            throw new EmployeeException(EMPLOYEE_MOVED_FAIL);
        }
    }

    @Override
    public void removeEmployee(UUID departmentId, UUID employeeId) {
        Employee currentEmployee = employeeRepositoryPort.getDetailEmployee(employeeId)
                .orElseThrow(() -> new EmployeeException(EMPLOYEE_NOT_FOUND));
        Department currentDepartment = departmentRepositoryPort.findById(departmentId)
                .orElseThrow(() -> new DepartmentException(DEPARTMENT_NOT_FOUND));
        if (!Objects.equals(currentEmployee.getDepartment(), currentDepartment.getDepartmentId())) {
            throw new EmployeeException(EMPLOYEE_NOT_IN_THIS_DEPARTMENT_YET);
        }

        boolean result = employeeRepositoryPort.removeEmployee(currentEmployee);

        if (!result) {
            throw new EmployeeException(EMPLOYEE_REMOVED_FAIL);
        }
    }

    @Override
    public void setEmployeeType(UUID departmentId, UUID employeeId, EmployeeType employeeType) {
        Employee currentEmployee = employeeRepositoryPort.getDetailEmployee(employeeId)
                .orElseThrow(() -> new EmployeeException(EMPLOYEE_NOT_FOUND));
        Department currentDepartment = departmentRepositoryPort.findById(departmentId)
                .orElseThrow(() -> new DepartmentException(DEPARTMENT_NOT_FOUND));
        if (!Objects.equals(currentEmployee.getDepartment(), currentDepartment.getDepartmentId())) {
            throw new EmployeeException(EMPLOYEE_NOT_IN_THIS_DEPARTMENT_YET);
        }
        boolean isExistHomeTeacher = currentDepartment.getEmployeeIds().stream()
                .anyMatch(id -> {
                    Optional<Employee> employee = employeeRepositoryPort.getDetailEmployee(id);
                    if (employee.isPresent()) {
                        return employee.get().getEmployeeType() == EmployeeType.HOMEROOM_TEACHER;
                    } else {
                        return false;
                    }
                });
        if (isExistHomeTeacher) {
            throw new EmployeeException(EMPLOYEE_SET_FAIL_TYPE);
        }

        boolean result = employeeRepositoryPort.setEmployee(
                currentEmployee,
                employeeType);

        if (!result) {
            throw new EmployeeException(EMPLOYEE_SET_FAIL);
        }
    }
}
