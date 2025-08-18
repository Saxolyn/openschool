package com.openschool.student.port.out;

import com.openschool.common.pageable.PageInfo;
import com.openschool.common.pageable.PageResult;
import com.openschool.domain.student.Student;
import com.openschool.domain.student.StudentStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StudentRepositoryPort {
    
    Student create(Student student);
    
    Student update(Student student);
    
    Optional<Student> findById(UUID studentId);
    
    Optional<Student> findByStudentCode(String studentCode);
    
    Optional<Student> findByEmail(String email);
    
    List<Student> findByClassId(UUID classId);
    
    List<Student> findByGradeId(UUID gradeId);
    
    List<Student> findByGuardianId(UUID guardianId);
    
    PageResult<Student> findAll(PageInfo pageInfo);
    
    PageResult<Student> findByStatus(StudentStatus status, PageInfo pageInfo);
    
    PageResult<Student> search(String searchTerm, PageInfo pageInfo);
    
    boolean existsByStudentCode(String studentCode);
    
    boolean existsByEmail(String email);
    
    boolean delete(UUID studentId);
    
    long countByClassId(UUID classId);
    
    long countByGradeId(UUID gradeId);
}
