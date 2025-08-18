package com.openschool.education.schoolclass.service;

import com.openschool.common.exception.DataNotFound;
import com.openschool.domain.schoolclass.model.ClassStatus;
import com.openschool.domain.schoolclass.model.SchoolClass;
import com.openschool.domain.student.Student;
import com.openschool.education.schoolclass.exception.ExceptionMessage;
import com.openschool.education.schoolclass.exception.SchoolClassException;
import com.openschool.education.schoolclass.port.in.*;
import com.openschool.education.schoolclass.port.in.command.*;
import com.openschool.schoolclass.port.in.*;
import com.openschool.common.pageable.PageInfo;
import com.openschool.common.pageable.PageResult;
import com.openschool.common.excel.result.ExportResult;
import com.openschool.schoolclass.port.in.command.*;

import com.openschool.education.schoolclass.port.out.SchoolClassRepositoryPort;
import com.openschool.education.student.exception.StudentException;
import com.openschool.education.student.port.out.StudentRepositoryPort;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
public class SchoolClassService implements
        CreateClassUseCase,
        EnrollStudentToClassUseCase,
        ListStudentsInClassUseCase,
        TransferStudentBetweenClassesUseCase,
        CloseClassUseCase,
        ArchiveClassUseCase,
        ExportClassListUseCase,
        SearchClassesUseCase,
        UpdateClassInfoUseCase,
        RemoveStudentFromClassUseCase {
    
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
            throw new DataNotFound(com.openschool.education.student.exception.ExceptionMessage.STUDENT_NOT_FOUND.getMessage());
        }
        
        Student student = studentOpt.get();
        
        // Check if student is active
        if (!student.isActive()) {
            throw new StudentException(com.openschool.education.student.exception.ExceptionMessage.STUDENT_NOT_ACTIVE.getMessage());
        }
        
        // Check if student can enroll
        if (!student.canEnrollInClass()) {
            throw new StudentException(com.openschool.education.student.exception.ExceptionMessage.STUDENT_ALREADY_ENROLLED.getMessage());
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

    @Override
    public PageResult<Student> listStudentsInClass(UUID classId, PageInfo pageInfo) {
        return studentRepository.findByClassId(classId, pageInfo);
    }

    @Override
    public void transferStudentBetweenClasses(TransferStudentBetweenClassesCommand command) {
        // Validate student exists and is in the from class
        Optional<Student> studentOpt = studentRepository.findById(command.getStudentId());
        if (studentOpt.isEmpty()) {
            throw new DataNotFound("Student not found with id: " + command.getStudentId());
        }

        Student student = studentOpt.get();
        if (!student.getCurrentClassId().equals(command.getFromClassId())) {
            throw new StudentException(ExceptionMessage.STUDENT_NOT_IN_CLASS.getMessage());
        }

        // Validate target class exists and can accept students
        Optional<SchoolClass> toClassOpt = schoolClassRepository.findById(command.getToClassId());
        if (toClassOpt.isEmpty()) {
            throw new DataNotFound("Target class not found with id: " + command.getToClassId());
        }

        SchoolClass toClass = toClassOpt.get();
        if (!toClass.canEnrollStudent()) {
            throw new SchoolClassException(ExceptionMessage.CLASS_CANNOT_ACCEPT_STUDENTS.getMessage());
        }

        // Update student's class
        student.setCurrentClassId(command.getToClassId());
        student.setUpdatedAt(LocalDateTime.now());
        studentRepository.update(student);

        // Update class student counts
        updateClassStudentCount(command.getFromClassId(), -1);
        updateClassStudentCount(command.getToClassId(), 1);
    }

    @Override
    public void closeClass(UUID classId) {
        Optional<SchoolClass> classOpt = schoolClassRepository.findById(classId);
        if (classOpt.isEmpty()) {
            throw new DataNotFound("Class not found with id: " + classId);
        }

        SchoolClass schoolClass = classOpt.get();
        schoolClass.setStatus(ClassStatus.CLOSED);
        schoolClass.setUpdatedAt(LocalDateTime.now());

        schoolClassRepository.update(schoolClass);
    }

    @Override
    public void archiveClass(UUID classId) {
        Optional<SchoolClass> classOpt = schoolClassRepository.findById(classId);
        if (classOpt.isEmpty()) {
            throw new DataNotFound("Class not found with id: " + classId);
        }

        SchoolClass schoolClass = classOpt.get();
        schoolClass.setStatus(ClassStatus.ARCHIVED);
        schoolClass.setUpdatedAt(LocalDateTime.now());

        schoolClassRepository.update(schoolClass);
    }

    @Override
    public ExportResult exportClassList(String fileName) {
        // This would be implemented with Excel export logic
        throw new UnsupportedOperationException("Export functionality not yet implemented");
    }

    @Override
    public PageResult<SchoolClass> searchClasses(String searchTerm, PageInfo pageInfo) {
        return schoolClassRepository.search(searchTerm, pageInfo);
    }

    @Override
    public SchoolClass updateClassInfo(UpdateClassInfoCommand command) {
        Optional<SchoolClass> classOpt = schoolClassRepository.findById(command.getClassId());
        if (classOpt.isEmpty()) {
            throw new DataNotFound("Class not found with id: " + command.getClassId());
        }

        SchoolClass schoolClass = classOpt.get();

        if (command.getName() != null) {
            schoolClass.setName(command.getName());
        }
        if (command.getCode() != null) {
            schoolClass.setCode(command.getCode());
        }
        if (command.getDescription() != null) {
            schoolClass.setDescription(command.getDescription());
        }
        if (command.getMaxStudents() != null) {
            schoolClass.setMaxStudents(command.getMaxStudents());
        }
        if (command.getHomeroomTeacherId() != null) {
            schoolClass.setHomeroomTeacherId(command.getHomeroomTeacherId());
        }

        schoolClass.setUpdatedAt(LocalDateTime.now());
        return schoolClassRepository.update(schoolClass);
    }

    @Override
    public void removeStudentFromClass(RemoveStudentFromClassCommand command) {
        // Validate student exists and is in the class
        Optional<Student> studentOpt = studentRepository.findById(command.getStudentId());
        if (studentOpt.isEmpty()) {
            throw new DataNotFound("Student not found with id: " + command.getStudentId());
        }

        Student student = studentOpt.get();
        if (!student.getCurrentClassId().equals(command.getClassId())) {
            throw new StudentException(ExceptionMessage.STUDENT_NOT_IN_CLASS.getMessage());
        }

        // Remove student from class
        student.setCurrentClassId(null);
        student.setUpdatedAt(LocalDateTime.now());
        studentRepository.update(student);

        // Update class student count
        updateClassStudentCount(command.getClassId(), -1);
    }

    // Helper method
    private void updateClassStudentCount(UUID classId, int delta) {
        Optional<SchoolClass> classOpt = schoolClassRepository.findById(classId);
        if (classOpt.isPresent()) {
            SchoolClass schoolClass = classOpt.get();
            int newCount = (schoolClass.getCurrentStudentCount() != null ? schoolClass.getCurrentStudentCount() : 0) + delta;
            schoolClass.setCurrentStudentCount(Math.max(0, newCount));
            schoolClass.setUpdatedAt(LocalDateTime.now());
            schoolClassRepository.update(schoolClass);
        }
    }
}
