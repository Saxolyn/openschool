package com.openschool.infrastructure.config;

import com.openschool.education.academic.port.in.CreateAcademicYearUseCase;
import com.openschool.education.academic.port.out.AcademicYearRepositoryPort;
import com.openschool.education.academic.service.AcademicYearService;
import com.openschool.administration.school.port.out.SchoolRepositoryPort;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AcademicConfig {
    @Bean
    public AcademicYearService getAcademicYearService(
            AcademicYearRepositoryPort academicYearRepositoryPort,
            SchoolRepositoryPort schoolRepositoryPort
    ) {
        return new AcademicYearService(academicYearRepositoryPort, schoolRepositoryPort);
    }

    @Bean
    @Qualifier("createAcademicYearUseCase")
    public CreateAcademicYearUseCase getCreateAcademicYearUseCase(AcademicYearService academicYearService) {
        return academicYearService;
    }
}
