package com.openschool.infrastructure.adapter.out.persistence.student.repository.jpa;

import com.openschool.domain.student.StudentStatus;
import com.openschool.infrastructure.adapter.out.persistence.student.entity.StudentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaStudentRepository extends JpaRepository<StudentEntity, UUID> {
    
    Optional<StudentEntity> findByStudentCode(String studentCode);
    
    Optional<StudentEntity> findByEmail(String email);
    
    List<StudentEntity> findByCurrentClassId(UUID classId);
    
    List<StudentEntity> findByCurrentGradeId(UUID gradeId);
    
    Page<StudentEntity> findByStatus(StudentStatus status, Pageable pageable);
    
    @Query("SELECT s FROM StudentEntity s WHERE " +
           "LOWER(s.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(s.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(s.studentCode) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(s.email) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<StudentEntity> search(@Param("searchTerm") String searchTerm, Pageable pageable);
    
    boolean existsByStudentCode(String studentCode);
    
    boolean existsByEmail(String email);
    
    long countByCurrentClassId(UUID classId);
    
    long countByCurrentGradeId(UUID gradeId);
}
