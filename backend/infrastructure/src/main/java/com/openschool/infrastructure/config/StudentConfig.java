package com.openschool.infrastructure.config;

import com.openschool.education.student.port.in.CreateStudentProfileUseCase;
import com.openschool.education.student.port.in.LinkParentToStudentUseCase;
import com.openschool.education.student.port.in.UpdateStudentProfileUseCase;
import com.openschool.education.student.port.out.GuardianRepositoryPort;
import com.openschool.education.student.port.out.StudentGuardianRepositoryPort;
import com.openschool.education.student.port.out.StudentRepositoryPort;
import com.openschool.education.student.service.GuardianService;
import com.openschool.education.student.service.StudentService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StudentConfig {
    
    @Bean
    public StudentService studentService(
            StudentRepositoryPort studentRepositoryPort,
            GuardianRepositoryPort guardianRepositoryPort,
            StudentGuardianRepositoryPort studentGuardianRepositoryPort
    ) {
        return new StudentService(studentRepositoryPort, guardianRepositoryPort, studentGuardianRepositoryPort);
    }
    
    @Bean
    public GuardianService guardianService(
            GuardianRepositoryPort guardianRepositoryPort
    ) {
        return new GuardianService(guardianRepositoryPort);
    }
    
    @Bean
    @Qualifier("createStudentProfileUseCase")
    public CreateStudentProfileUseCase createStudentProfileUseCase(StudentService studentService) {
        return studentService;
    }
    
    @Bean
    @Qualifier("updateStudentProfileUseCase")
    public UpdateStudentProfileUseCase updateStudentProfileUseCase(StudentService studentService) {
        return studentService;
    }
    
    @Bean
    @Qualifier("linkParentToStudentUseCase")
    public LinkParentToStudentUseCase linkParentToStudentUseCase(StudentService studentService) {
        return studentService;
    }
}
