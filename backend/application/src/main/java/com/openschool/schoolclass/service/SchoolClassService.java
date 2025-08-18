package com.openschool.schoolclass.service;

import com.openschool.common.exception.DataNotFound;
import com.openschool.domain.schoolclass.model.ClassStatus;
import com.openschool.domain.schoolclass.model.SchoolClass;
import com.openschool.domain.student.Student;
import com.openschool.schoolclass.exception.ExceptionMessage;
import com.openschool.schoolclass.exception.SchoolClassException;
import com.openschool.schoolclass.port.in.CreateClassUseCase;
import com.openschool.schoolclass.port.in.EnrollStudentToClassUseCase;
import com.openschool.schoolclass.port.in.command.CreateClassCommand;
import com.openschool.schoolclass.port.in.command.EnrollStudentCommand;
import com.openschool.schoolclass.port.out.SchoolClassRepositoryPort;
import com.openschool.student.exception.StudentException;
import com.openschool.student.port.out.StudentRepositoryPort;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
public class SchoolClassService implements 
        CreateClassUseCase,
        EnrollStudentToClassUseCase {
    
    private final SchoolClassRepositoryPort schoolClassRepository;
    private final StudentRepositoryPort studentRepository;
    
    @Override
    public SchoolClass createClass(CreateClassCommand command) {
        // Validate class code uniqueness
        if (schoolClassRepository.existsByCode(command.getCode())) {
            throw new SchoolClassException(ExceptionMessage.CLASS_CODE_ALREADY_EXISTS.getMessage());
        }
        
        // Validate class name uniqueness within grade and academic year
        if (schoolClassRepository.existsByName(command.getName(), command.getGradeId(), command.getAcademicYearId())) {
            throw new SchoolClassException(ExceptionMessage.CLASS_NAME_ALREADY_EXISTS.getMessage());
        }
        
        // Create school class domain object
        SchoolClass schoolClass = SchoolClass.builder()
                .id(UUID.randomUUID())
                .name(command.getName())
                .code(command.getCode())
                .description(command.getDescription())
                .schoolId(command.getSchoolId())
                .gradeId(command.getGradeId())
                .academicYearId(command.getAcademicYearId())
                .maxStudents(command.getMaxStudents())
                .currentStudentCount(0)
                .homeroomTeacherId(command.getHomeroomTeacherId())
                .status(ClassStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        
        return schoolClassRepository.create(schoolClass);
    }
    
    @Override
    public boolean enrollStudent(EnrollStudentCommand command) {
        // Find and validate student
        Optional<Student> studentOpt = studentRepository.findById(command.getStudentId());
        if (studentOpt.isEmpty()) {
            throw new DataNotFound(com.openschool.student.exception.ExceptionMessage.STUDENT_NOT_FOUND.getMessage());
        }
        
        Student student = studentOpt.get();
        
        // Check if student is active
        if (!student.isActive()) {
            throw new StudentException(com.openschool.student.exception.ExceptionMessage.STUDENT_NOT_ACTIVE.getMessage());
        }
        
        // Check if student can enroll
        if (!student.canEnrollInClass()) {
            throw new StudentException(com.openschool.student.exception.ExceptionMessage.STUDENT_ALREADY_ENROLLED.getMessage());
        }
        
        // Find and validate class
        Optional<SchoolClass> classOpt = schoolClassRepository.findById(command.getClassId());
        if (classOpt.isEmpty()) {
            throw new DataNotFound(ExceptionMessage.CLASS_NOT_FOUND.getMessage());
        }
        
        SchoolClass schoolClass = classOpt.get();
        
        // Check if class is active
        if (!schoolClass.isActive()) {
            throw new SchoolClassException(ExceptionMessage.CLASS_NOT_ACTIVE.getMessage());
        }
        
        // Check if class can enroll students
        if (!schoolClass.canEnrollStudent()) {
            throw new SchoolClassException(ExceptionMessage.CLASS_IS_FULL.getMessage());
        }
        
        // Update student's current class
        student.setCurrentClassId(command.getClassId());
        student.setUpdatedAt(LocalDateTime.now());
        studentRepository.update(student);
        
        // Update class student count
        schoolClass.incrementStudentCount();
        schoolClass.setUpdatedAt(LocalDateTime.now());
        schoolClassRepository.update(schoolClass);
        
        return true;
    }
}
