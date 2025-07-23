package com.openschool.infrastructure.adapter.in.rest.department;

import com.openschool.department.port.in.CreateDepartmentUseCase;
import com.openschool.department.port.in.DeleteDepartmentUseCase;
import com.openschool.department.port.in.GetListDepartmentUseCase;
import com.openschool.department.port.in.UpdateDepartmentUseCase;
import com.openschool.domain.department.Department;
import com.openschool.infrastructure.adapter.in.rest.department.dto.CreatedDepartmentDto;
import com.openschool.infrastructure.adapter.out.persistence.department.repository.DepartmentRepositoryAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.openschool.infrastructure.adapter.in.rest.department.mapper.DepartmentMapper.dtoToDepartmentCommand;
import static com.openschool.infrastructure.adapter.in.rest.department.mapper.DepartmentMapper.toDepartmentDto;

@RestController
@RequestMapping("/api/v1/department")
@RequiredArgsConstructor
public class DepartmentController {

    private final CreateDepartmentUseCase createDepartmentUseCase;
    private final UpdateDepartmentUseCase updateDepartmentUseCase;
    private final GetListDepartmentUseCase listDepartmentUseCase;
    private final DeleteDepartmentUseCase deleteDepartmentUseCase;
    private final DepartmentRepositoryAdapter departmentRepositoryAdapter;

    public CreatedDepartmentDto createDepartment(CreatedDepartmentDto createDepartmentDto) {
        Department department =  createDepartmentUseCase.createDepartment(dtoToDepartmentCommand(createDepartmentDto));
        return toDepartmentDto(departmentRepositoryAdapter.save(department));

    }


}
