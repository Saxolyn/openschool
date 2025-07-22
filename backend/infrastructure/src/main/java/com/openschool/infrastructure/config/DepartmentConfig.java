package com.openschool.infrastructure.config;

import com.openschool.department.port.in.CreateDepartmentUseCase;
import com.openschool.department.port.in.UpdateDepartmentUseCase;
import com.openschool.department.port.out.DepartmentRepositoryPort;
import com.openschool.department.service.DepartmentService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DepartmentConfig {

    @Bean
    public DepartmentService departmentConfig(DepartmentRepositoryPort departmentRepositoryPort) {
        return new DepartmentService(departmentRepositoryPort);
    }

    @Bean
    public CreateDepartmentUseCase createDepartmentUseCase(DepartmentService departmentService) {
        return departmentService;
    }

    @Bean
    public UpdateDepartmentUseCase updateDepartmentUseCase(DepartmentService departmentService) {
        return departmentService;
    }
}
