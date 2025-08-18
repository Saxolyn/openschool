package com.openschool.schoolclass.port.out;

import com.openschool.common.pageable.PageInfo;
import com.openschool.common.pageable.PageResult;
import com.openschool.domain.schoolclass.model.ClassStatus;
import com.openschool.domain.schoolclass.model.SchoolClass;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SchoolClassRepositoryPort {
    
    SchoolClass create(SchoolClass schoolClass);
    
    SchoolClass update(SchoolClass schoolClass);
    
    Optional<SchoolClass> findById(UUID classId);
    
    Optional<SchoolClass> findByCode(String code);
    
    List<SchoolClass> findByGradeId(UUID gradeId);
    
    List<SchoolClass> findByAcademicYearId(UUID academicYearId);
    
    List<SchoolClass> findByHomeroomTeacherId(UUID teacherId);
    
    PageResult<SchoolClass> findAll(PageInfo pageInfo);
    
    PageResult<SchoolClass> findByStatus(ClassStatus status, PageInfo pageInfo);
    
    PageResult<SchoolClass> search(String searchTerm, PageInfo pageInfo);
    
    boolean existsByCode(String code);
    
    boolean existsByName(String name, UUID gradeId, UUID academicYearId);
    
    boolean delete(UUID classId);
    
    long countByGradeId(UUID gradeId);
    
    long countByAcademicYearId(UUID academicYearId);
}
