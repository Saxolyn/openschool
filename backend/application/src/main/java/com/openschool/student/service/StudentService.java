package com.openschool.student.service;

import com.openschool.common.exception.DataNotFound;
import com.openschool.domain.student.Guardian;
import com.openschool.domain.student.Student;
import com.openschool.domain.student.StudentStatus;
import com.openschool.student.exception.ExceptionMessage;
import com.openschool.student.exception.StudentException;
import com.openschool.student.port.in.CreateStudentProfileUseCase;
import com.openschool.student.port.in.LinkParentToStudentUseCase;
import com.openschool.student.port.in.UpdateStudentProfileUseCase;
import com.openschool.student.port.in.command.CreateStudentCommand;
import com.openschool.student.port.in.command.LinkParentCommand;
import com.openschool.student.port.in.command.UpdateStudentCommand;
import com.openschool.student.port.out.GuardianRepositoryPort;
import com.openschool.student.port.out.StudentGuardianRepositoryPort;
import com.openschool.student.port.out.StudentRepositoryPort;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
public class StudentService implements 
        CreateStudentProfileUseCase,
        UpdateStudentProfileUseCase,
        LinkParentToStudentUseCase {
    
    private final StudentRepositoryPort studentRepository;
    private final GuardianRepositoryPort guardianRepository;
    private final StudentGuardianRepositoryPort studentGuardianRepository;
    
    @Override
    public Student createStudent(CreateStudentCommand command) {
        // Validate student code uniqueness
        if (studentRepository.existsByStudentCode(command.getStudentCode())) {
            throw new StudentException(ExceptionMessage.STUDENT_CODE_ALREADY_EXISTS.getMessage());
        }
        
        // Validate email uniqueness if provided
        if (command.getEmail() != null && studentRepository.existsByEmail(command.getEmail())) {
            throw new StudentException(ExceptionMessage.STUDENT_EMAIL_ALREADY_EXISTS.getMessage());
        }
        
        // Create student domain object
        Student student = Student.builder()
                .id(UUID.randomUUID())
                .studentCode(command.getStudentCode())
                .firstName(command.getFirstName())
                .lastName(command.getLastName())
                .dateOfBirth(command.getDateOfBirth())
                .gender(command.getGender())
                .email(command.getEmail())
                .phoneNumber(command.getPhoneNumber())
                .address(command.getAddress())
                .schoolId(command.getSchoolId())
                .currentGradeId(command.getGradeId())
                .enrollmentDate(command.getEnrollmentDate())
                .status(StudentStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        
        return studentRepository.create(student);
    }
    
    @Override
    public Student updateStudent(UpdateStudentCommand command) {
        // Find existing student
        Optional<Student> existingStudent = studentRepository.findById(command.getStudentId());
        if (existingStudent.isEmpty()) {
            throw new DataNotFound(ExceptionMessage.STUDENT_NOT_FOUND.getMessage());
        }
        
        Student student = existingStudent.get();
        
        // Validate email uniqueness if changed
        if (command.getEmail() != null && 
            !command.getEmail().equals(student.getEmail()) &&
            studentRepository.existsByEmail(command.getEmail())) {
            throw new StudentException(ExceptionMessage.STUDENT_EMAIL_ALREADY_EXISTS.getMessage());
        }
        
        // Update fields
        student.setFirstName(command.getFirstName());
        student.setLastName(command.getLastName());
        student.setDateOfBirth(command.getDateOfBirth());
        student.setGender(command.getGender());
        student.setEmail(command.getEmail());
        student.setPhoneNumber(command.getPhoneNumber());
        student.setAddress(command.getAddress());
        student.setUpdatedAt(LocalDateTime.now());
        
        return studentRepository.update(student);
    }
    
    @Override
    public boolean linkParentToStudent(LinkParentCommand command) {
        // Validate student exists
        Optional<Student> student = studentRepository.findById(command.getStudentId());
        if (student.isEmpty()) {
            throw new DataNotFound(ExceptionMessage.STUDENT_NOT_FOUND.getMessage());
        }
        
        // Validate guardian exists
        Optional<Guardian> guardian = guardianRepository.findById(command.getGuardianId());
        if (guardian.isEmpty()) {
            throw new DataNotFound(ExceptionMessage.GUARDIAN_NOT_FOUND.getMessage());
        }
        
        // Check if already linked
        if (studentGuardianRepository.existsRelationship(command.getStudentId(), command.getGuardianId())) {
            throw new StudentException(ExceptionMessage.GUARDIAN_ALREADY_LINKED.getMessage());
        }
        
        return studentGuardianRepository.linkStudentToGuardian(
                command.getStudentId(), 
                command.getGuardianId(), 
                command.getRelationship()
        );
    }
}
