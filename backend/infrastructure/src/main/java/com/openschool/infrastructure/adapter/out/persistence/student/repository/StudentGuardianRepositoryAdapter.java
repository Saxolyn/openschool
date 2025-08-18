package com.openschool.infrastructure.adapter.out.persistence.student.repository;

import com.openschool.domain.student.GuardianRelationship;
import com.openschool.infrastructure.adapter.out.persistence.student.entity.GuardianEntity;
import com.openschool.infrastructure.adapter.out.persistence.student.entity.StudentEntity;
import com.openschool.infrastructure.adapter.out.persistence.student.entity.StudentGuardianEntity;
import com.openschool.infrastructure.adapter.out.persistence.student.repository.jpa.JpaStudentGuardianRepository;
import com.openschool.education.student.port.out.StudentGuardianRepositoryPort;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@AllArgsConstructor
public class StudentGuardianRepositoryAdapter implements StudentGuardianRepositoryPort {
    
    private final JpaStudentGuardianRepository jpaStudentGuardianRepository;
    
    @Override
    public boolean linkStudentToGuardian(UUID studentId, UUID guardianId, GuardianRelationship relationship) {
        try {
            StudentEntity student = StudentEntity.builder().id(studentId).build();
            GuardianEntity guardian = GuardianEntity.builder().id(guardianId).build();
            
            StudentGuardianEntity entity = StudentGuardianEntity.builder()
                    .id(UUID.randomUUID())
                    .student(student)
                    .guardian(guardian)
                    .relationship(relationship)
                    .build();
            
            jpaStudentGuardianRepository.save(entity);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public boolean unlinkStudentFromGuardian(UUID studentId, UUID guardianId) {
        try {
            jpaStudentGuardianRepository.deleteByStudentIdAndGuardianId(studentId, guardianId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public List<UUID> findGuardianIdsByStudentId(UUID studentId) {
        return jpaStudentGuardianRepository.findGuardianIdsByStudentId(studentId);
    }
    
    @Override
    public List<UUID> findStudentIdsByGuardianId(UUID guardianId) {
        return jpaStudentGuardianRepository.findStudentIdsByGuardianId(guardianId);
    }
    
    @Override
    public GuardianRelationship findRelationship(UUID studentId, UUID guardianId) {
        Optional<StudentGuardianEntity> entity = jpaStudentGuardianRepository
                .findByStudentIdAndGuardianId(studentId, guardianId);
        return entity.map(StudentGuardianEntity::getRelationship).orElse(null);
    }
    
    @Override
    public boolean existsRelationship(UUID studentId, UUID guardianId) {
        return jpaStudentGuardianRepository.existsByStudentIdAndGuardianId(studentId, guardianId);
    }
}
