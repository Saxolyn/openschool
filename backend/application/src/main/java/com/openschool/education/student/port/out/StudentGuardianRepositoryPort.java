package com.openschool.education.student.port.out;

import com.openschool.domain.student.GuardianRelationship;

import java.util.List;
import java.util.UUID;

public interface StudentGuardianRepositoryPort {
    
    boolean linkStudentToGuardian(UUID studentId, UUID guardianId, GuardianRelationship relationship);
    
    boolean unlinkStudentFromGuardian(UUID studentId, UUID guardianId);
    
    List<UUID> findGuardianIdsByStudentId(UUID studentId);
    
    List<UUID> findStudentIdsByGuardianId(UUID guardianId);
    
    GuardianRelationship findRelationship(UUID studentId, UUID guardianId);
    
    boolean existsRelationship(UUID studentId, UUID guardianId);
}
