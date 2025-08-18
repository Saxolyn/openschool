package com.openschool.education.student.service;

import com.openschool.common.exception.DataNotFound;
import com.openschool.domain.student.Guardian;
import com.openschool.domain.student.Student;
import com.openschool.domain.student.StudentStatus;
import com.openschool.education.student.exception.ExceptionMessage;
import com.openschool.education.student.exception.StudentException;
import com.openschool.education.student.port.in.*;
import com.openschool.education.student.port.in.command.*;
import com.openschool.student.port.in.*;
import com.openschool.common.pageable.PageInfo;
import com.openschool.common.pageable.PageResult;
import com.openschool.common.excel.command.ImportExcelCommand;
import com.openschool.common.excel.result.ImportResult;
import com.openschool.common.excel.result.ExportResult;
import com.openschool.student.port.in.command.*;

import java.util.List;
import com.openschool.education.student.port.out.GuardianRepositoryPort;
import com.openschool.education.student.port.out.StudentGuardianRepositoryPort;
import com.openschool.education.student.port.out.StudentRepositoryPort;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
public class StudentService implements
        CreateStudentProfileUseCase,
        UpdateStudentProfileUseCase,
        LinkParentToStudentUseCase,
        SearchStudentUseCase,
        ArchiveStudentUseCase,
        TransferStudentUseCase,
        AssignStudentToClassUseCase,
        ViewStudentGuardianInfoUseCase,
        ExportStudentListUseCase,
        ImportStudentListUseCase {
    
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

    @Override
    public PageResult<Student> searchStudent(String searchTerm, PageInfo pageInfo) {
        return studentRepository.search(searchTerm, pageInfo);
    }

    @Override
    public void archiveStudent(UUID studentId) {
        Optional<Student> studentOpt = studentRepository.findById(studentId);
        if (studentOpt.isEmpty()) {
            throw new DataNotFound("Student not found with id: " + studentId);
        }

        Student student = studentOpt.get();
        student.setStatus(StudentStatus.ARCHIVED);
        student.setUpdatedAt(LocalDateTime.now());

        studentRepository.update(student);
    }

    @Override
    public void transferStudent(TransferStudentCommand command) {
        Optional<Student> studentOpt = studentRepository.findById(command.getStudentId());
        if (studentOpt.isEmpty()) {
            throw new DataNotFound("Student not found with id: " + command.getStudentId());
        }

        Student student = studentOpt.get();

        // Update student information
        if (command.getNewSchoolId() != null) {
            student.setSchoolId(command.getNewSchoolId());
        }
        if (command.getNewGradeId() != null) {
            student.setCurrentGradeId(command.getNewGradeId());
        }
        if (command.getNewClassId() != null) {
            student.setCurrentClassId(command.getNewClassId());
        }

        student.setUpdatedAt(LocalDateTime.now());
        studentRepository.update(student);
    }

    @Override
    public void assignStudentToClass(AssignStudentToClassCommand command) {
        Optional<Student> studentOpt = studentRepository.findById(command.getStudentId());
        if (studentOpt.isEmpty()) {
            throw new DataNotFound("Student not found with id: " + command.getStudentId());
        }

        Student student = studentOpt.get();
        student.setCurrentClassId(command.getClassId());
        student.setUpdatedAt(LocalDateTime.now());

        studentRepository.update(student);
    }

    @Override
    public List<Guardian> viewStudentGuardianInfo(UUID studentId) {
        return guardianRepository.findByStudentId(studentId);
    }

    @Override
    public ExportResult exportStudentList(String fileName) {
        // This would be implemented with Excel export logic
        // For now, return a placeholder
        throw new UnsupportedOperationException("Export functionality not yet implemented");
    }

    @Override
    public ImportResult<Student> importStudentList(ImportExcelCommand command) {
        // This would be implemented with Excel import logic
        // For now, return a placeholder
        throw new UnsupportedOperationException("Import functionality not yet implemented");
    }
}
