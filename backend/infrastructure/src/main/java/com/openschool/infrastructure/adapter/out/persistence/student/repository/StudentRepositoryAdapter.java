package com.openschool.infrastructure.adapter.out.persistence.student.repository;

import com.openschool.common.pageable.PageInfo;
import com.openschool.common.pageable.PageResult;
import com.openschool.domain.student.Student;
import com.openschool.domain.student.StudentStatus;
import com.openschool.infrastructure.adapter.out.persistence.grade.entity.GradeEntity;
import com.openschool.infrastructure.adapter.out.persistence.school.entity.SchoolEntity;
import com.openschool.infrastructure.adapter.out.persistence.schoolclass.entity.SchoolClassEntity;
import com.openschool.infrastructure.adapter.out.persistence.student.entity.StudentEntity;
import com.openschool.infrastructure.adapter.out.persistence.student.repository.jpa.JpaStudentRepository;
import com.openschool.education.student.port.out.StudentRepositoryPort;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@AllArgsConstructor
public class StudentRepositoryAdapter implements StudentRepositoryPort {
    
    private final JpaStudentRepository jpaStudentRepository;
    
    @Override
    public Student create(Student student) {
        SchoolEntity school = SchoolEntity.referenceOnly(student.getSchoolId());
        GradeEntity grade = student.getCurrentGradeId() != null ? 
                GradeEntity.referenceOnly(student.getCurrentGradeId()) : null;
        SchoolClassEntity schoolClass = student.getCurrentClassId() != null ? 
                SchoolClassEntity.referenceOnly(student.getCurrentClassId()) : null;
        
        StudentEntity entity = StudentEntity.fromDomain(student, school, grade, schoolClass);
        StudentEntity savedEntity = jpaStudentRepository.save(entity);
        return savedEntity.toDomain();
    }
    
    @Override
    public Student update(Student student) {
        return create(student); // Same logic for update
    }
    
    @Override
    public Optional<Student> findById(UUID studentId) {
        return jpaStudentRepository.findById(studentId)
                .map(StudentEntity::toDomain);
    }
    
    @Override
    public Optional<Student> findByStudentCode(String studentCode) {
        return jpaStudentRepository.findByStudentCode(studentCode)
                .map(StudentEntity::toDomain);
    }
    
    @Override
    public Optional<Student> findByEmail(String email) {
        return jpaStudentRepository.findByEmail(email)
                .map(StudentEntity::toDomain);
    }
    
    @Override
    public List<Student> findByClassId(UUID classId) {
        return jpaStudentRepository.findByCurrentClassId(classId)
                .stream()
                .map(StudentEntity::toDomain)
                .toList();
    }
    
    @Override
    public List<Student> findByGradeId(UUID gradeId) {
        return jpaStudentRepository.findByCurrentGradeId(gradeId)
                .stream()
                .map(StudentEntity::toDomain)
                .toList();
    }
    
    @Override
    public List<Student> findByGuardianId(UUID guardianId) {
        // This would require a join query - simplified for now
        return List.of();
    }
    
    @Override
    public PageResult<Student> findAll(PageInfo pageInfo) {
        Pageable pageable = PageRequest.of(pageInfo.getPage(), pageInfo.getSize());
        Page<StudentEntity> page = jpaStudentRepository.findAll(pageable);
        
        List<Student> students = page.getContent()
                .stream()
                .map(StudentEntity::toDomain)
                .toList();
        
        return new PageResult<>(students, page.getTotalElements(), page.getTotalPages());
    }
    
    @Override
    public PageResult<Student> findByStatus(StudentStatus status, PageInfo pageInfo) {
        Pageable pageable = PageRequest.of(pageInfo.getPage(), pageInfo.getSize());
        Page<StudentEntity> page = jpaStudentRepository.findByStatus(status, pageable);
        
        List<Student> students = page.getContent()
                .stream()
                .map(StudentEntity::toDomain)
                .toList();
        
        return new PageResult<>(students, page.getTotalElements(), page.getTotalPages());
    }
    
    @Override
    public PageResult<Student> search(String searchTerm, PageInfo pageInfo) {
        Pageable pageable = PageRequest.of(pageInfo.getPage(), pageInfo.getSize());
        Page<StudentEntity> page = jpaStudentRepository.search(searchTerm, pageable);
        
        List<Student> students = page.getContent()
                .stream()
                .map(StudentEntity::toDomain)
                .toList();
        
        return new PageResult<>(students, page.getTotalElements(), page.getTotalPages());
    }
    
    @Override
    public boolean existsByStudentCode(String studentCode) {
        return jpaStudentRepository.existsByStudentCode(studentCode);
    }
    
    @Override
    public boolean existsByEmail(String email) {
        return jpaStudentRepository.existsByEmail(email);
    }
    
    @Override
    public boolean delete(UUID studentId) {
        if (jpaStudentRepository.existsById(studentId)) {
            jpaStudentRepository.deleteById(studentId);
            return true;
        }
        return false;
    }
    
    @Override
    public long countByClassId(UUID classId) {
        return jpaStudentRepository.countByCurrentClassId(classId);
    }
    
    @Override
    public long countByGradeId(UUID gradeId) {
        return jpaStudentRepository.countByCurrentGradeId(gradeId);
    }
}
