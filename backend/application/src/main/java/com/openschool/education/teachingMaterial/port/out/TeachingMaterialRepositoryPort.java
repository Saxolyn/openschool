package com.openschool.education.teachingMaterial.port.out;

import com.openschool.common.pageable.PageInfo;
import com.openschool.common.pageable.PageResult;
import com.openschool.domain.teachingmaterial.TeachingMaterial;

import java.util.Optional;
import java.util.UUID;

public interface TeachingMaterialRepositoryPort {
    
    TeachingMaterial create(TeachingMaterial material);
    
    TeachingMaterial update(TeachingMaterial material);
    
    Optional<TeachingMaterial> findById(UUID materialId);
    
    PageResult<TeachingMaterial> findAll(PageInfo pageInfo);
    
    PageResult<TeachingMaterial> findBySubjectId(UUID subjectId, PageInfo pageInfo);
    
    PageResult<TeachingMaterial> findByGradeId(UUID gradeId, PageInfo pageInfo);
    
    PageResult<TeachingMaterial> findByTeacherId(UUID teacherId, PageInfo pageInfo);
    
    PageResult<TeachingMaterial> findByType(String type, PageInfo pageInfo);
    
    PageResult<TeachingMaterial> findBySchoolId(UUID schoolId, PageInfo pageInfo);
    
    boolean delete(UUID materialId);
    
    long count();
    
    long countBySubjectId(UUID subjectId);
}
