package com.openschool.infrastructure.adapter.out.persistence.student.repository.jpa;

import com.openschool.infrastructure.adapter.out.persistence.student.entity.StudentGuardianEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaStudentGuardianRepository extends JpaRepository<StudentGuardianEntity, UUID> {
    
    @Query("SELECT sg.guardian.id FROM StudentGuardianEntity sg WHERE sg.student.id = :studentId")
    List<UUID> findGuardianIdsByStudentId(@Param("studentId") UUID studentId);
    
    @Query("SELECT sg.student.id FROM StudentGuardianEntity sg WHERE sg.guardian.id = :guardianId")
    List<UUID> findStudentIdsByGuardianId(@Param("guardianId") UUID guardianId);
    
    @Query("SELECT sg FROM StudentGuardianEntity sg WHERE sg.student.id = :studentId AND sg.guardian.id = :guardianId")
    Optional<StudentGuardianEntity> findByStudentIdAndGuardianId(@Param("studentId") UUID studentId, 
                                                                 @Param("guardianId") UUID guardianId);
    
    boolean existsByStudentIdAndGuardianId(UUID studentId, UUID guardianId);
    
    void deleteByStudentIdAndGuardianId(UUID studentId, UUID guardianId);
}
