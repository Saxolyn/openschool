package com.openschool.infrastructure.adapter.out.persistence.schoolclass.repository.jpa;

import com.openschool.domain.schoolclass.model.ClassStatus;
import com.openschool.infrastructure.adapter.out.persistence.schoolclass.entity.SchoolClassEntity;
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
public interface JpaSchoolClassRepository extends JpaRepository<SchoolClassEntity, UUID> {
    
    Optional<SchoolClassEntity> findByCode(String code);
    
    List<SchoolClassEntity> findByGradeId(UUID gradeId);
    
    List<SchoolClassEntity> findByAcademicYearId(UUID academicYearId);
    
    List<SchoolClassEntity> findByHomeroomTeacherEmployeeId(UUID teacherId);
    
    Page<SchoolClassEntity> findByStatus(ClassStatus status, Pageable pageable);
    
    @Query("SELECT sc FROM SchoolClassEntity sc WHERE " +
           "LOWER(sc.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(sc.code) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(sc.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<SchoolClassEntity> search(@Param("searchTerm") String searchTerm, Pageable pageable);
    
    boolean existsByCode(String code);
    
    @Query("SELECT COUNT(sc) > 0 FROM SchoolClassEntity sc WHERE " +
           "sc.name = :name AND sc.grade.id = :gradeId AND sc.academicYear.id = :academicYearId")
    boolean existsByNameAndGradeIdAndAcademicYearId(@Param("name") String name, 
                                                    @Param("gradeId") UUID gradeId, 
                                                    @Param("academicYearId") UUID academicYearId);
    
    long countByGradeId(UUID gradeId);
    
    long countByAcademicYearId(UUID academicYearId);
}
