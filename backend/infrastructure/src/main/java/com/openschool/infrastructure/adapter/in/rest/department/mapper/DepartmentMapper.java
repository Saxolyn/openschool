package com.openschool.infrastructure.adapter.in.rest.department.mapper;

import com.openschool.department.port.in.command.CreatedDepartmentCommand;
import com.openschool.domain.department.Department;
import com.openschool.infrastructure.adapter.in.rest.department.dto.CreatedDepartmentDto;

public class DepartmentMapper {

    public static CreatedDepartmentDto toDepartmentDto(Department model) {
        return CreatedDepartmentDto.builder()
                .departmentName(model.getDepartmentName())
                .description(model.getDescription())
                .departmentHead(model.getDepartmentHead())
                .departmentDeputy(model.getDepartmentDeputy())
                .departmentCode(model.getDepartmentCode())
                .departmentEmail(model.getDepartmentEmail())
                .departmentPhone(model.getDepartmentPhone())
                .build();
    }

 public static CreatedDepartmentCommand dtoToDepartmentCommand(CreatedDepartmentDto dto){
        return CreatedDepartmentCommand.builder()
                .departmentName(dto.getDepartmentName())
                .description(dto.getDescription())
                .departmentHead(dto.getDepartmentHead())
                .departmentDeputy(dto.getDepartmentDeputy())
                .departmentCode(dto.getDepartmentCode())
                .departmentEmail(dto.getDepartmentEmail())
                .departmentPhone(dto.getDepartmentPhone())
                .build();
 }


}
