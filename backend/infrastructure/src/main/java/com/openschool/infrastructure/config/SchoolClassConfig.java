package com.openschool.infrastructure.config;

import com.openschool.education.schoolclass.port.in.CreateClassUseCase;
import com.openschool.education.schoolclass.port.in.EnrollStudentToClassUseCase;
import com.openschool.education.schoolclass.port.out.SchoolClassRepositoryPort;
import com.openschool.education.schoolclass.service.SchoolClassService;
import com.openschool.education.student.port.out.StudentRepositoryPort;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SchoolClassConfig {
    
    @Bean
    public SchoolClassService schoolClassService(
            SchoolClassRepositoryPort schoolClassRepositoryPort,
            StudentRepositoryPort studentRepositoryPort
    ) {
        return new SchoolClassService(schoolClassRepositoryPort, studentRepositoryPort);
    }
    
    @Bean
    @Qualifier("createClassUseCase")
    public CreateClassUseCase createClassUseCase(SchoolClassService schoolClassService) {
        return schoolClassService;
    }
    
    @Bean
    @Qualifier("enrollStudentToClassUseCase")
    public EnrollStudentToClassUseCase enrollStudentToClassUseCase(SchoolClassService schoolClassService) {
        return schoolClassService;
    }
}
