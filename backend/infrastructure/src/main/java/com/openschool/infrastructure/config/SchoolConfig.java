package com.openschool.infrastructure.config;

import com.openschool.administration.school.port.in.CreateSchoolUseCase;
import com.openschool.administration.school.port.out.SchoolRepositoryPort;
import com.openschool.administration.school.service.SchoolService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SchoolConfig {
    @Bean
    public SchoolService getSchoolService(SchoolRepositoryPort schoolRepository) {
        return new SchoolService(schoolRepository);
    }

    @Bean
    @Qualifier("createSchoolUseCase")
    public CreateSchoolUseCase createSchoolUseCase(SchoolService schoolService) {
        return schoolService;
    }
}
