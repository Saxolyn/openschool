package com.openschool.infrastructure.adapter.out.persistence.schoolclass.repository;

import com.openschool.common.pageable.PageInfo;
import com.openschool.common.pageable.PageResult;
import com.openschool.domain.schoolclass.model.ClassStatus;
import com.openschool.domain.schoolclass.model.SchoolClass;
import com.openschool.infrastructure.adapter.out.persistence.academic.entity.AcademicYearEntity;
import com.openschool.infrastructure.adapter.out.persistence.employee.entity.EmployeeEntity;
import com.openschool.infrastructure.adapter.out.persistence.grade.entity.GradeEntity;
import com.openschool.infrastructure.adapter.out.persistence.school.entity.SchoolEntity;
import com.openschool.infrastructure.adapter.out.persistence.schoolclass.entity.SchoolClassEntity;
import com.openschool.infrastructure.adapter.out.persistence.schoolclass.repository.jpa.JpaSchoolClassRepository;
import com.openschool.schoolclass.port.out.SchoolClassRepositoryPort;
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
public class SchoolClassRepositoryAdapter implements SchoolClassRepositoryPort {
    
    private final JpaSchoolClassRepository jpaSchoolClassRepository;
    
    @Override
    public SchoolClass create(SchoolClass schoolClass) {
        SchoolEntity school = SchoolEntity.referenceOnly(schoolClass.getSchoolId());
        GradeEntity grade = GradeEntity.referenceOnly(schoolClass.getGradeId());
        AcademicYearEntity academicYear = AcademicYearEntity.referenceOnly(schoolClass.getAcademicYearId());
        EmployeeEntity homeroomTeacher = schoolClass.getHomeroomTeacherId() != null ? 
                EmployeeEntity.referenceOnly(schoolClass.getHomeroomTeacherId()) : null;
        
        SchoolClassEntity entity = SchoolClassEntity.fromDomain(schoolClass, school, grade, academicYear, homeroomTeacher);
        SchoolClassEntity savedEntity = jpaSchoolClassRepository.save(entity);
        return savedEntity.toDomain();
    }
    
    @Override
    public SchoolClass update(SchoolClass schoolClass) {
        return create(schoolClass); // Same logic for update
    }
    
    @Override
    public Optional<SchoolClass> findById(UUID classId) {
        return jpaSchoolClassRepository.findById(classId)
                .map(SchoolClassEntity::toDomain);
    }
    
    @Override
    public Optional<SchoolClass> findByCode(String code) {
        return jpaSchoolClassRepository.findByCode(code)
                .map(SchoolClassEntity::toDomain);
    }
    
    @Override
    public List<SchoolClass> findByGradeId(UUID gradeId) {
        return jpaSchoolClassRepository.findByGradeId(gradeId)
                .stream()
                .map(SchoolClassEntity::toDomain)
                .toList();
    }
    
    @Override
    public List<SchoolClass> findByAcademicYearId(UUID academicYearId) {
        return jpaSchoolClassRepository.findByAcademicYearId(academicYearId)
                .stream()
                .map(SchoolClassEntity::toDomain)
                .toList();
    }
    
    @Override
    public List<SchoolClass> findByHomeroomTeacherId(UUID teacherId) {
        return jpaSchoolClassRepository.findByHomeroomTeacherEmployeeId(teacherId)
                .stream()
                .map(SchoolClassEntity::toDomain)
                .toList();
    }
    
    @Override
    public PageResult<SchoolClass> findAll(PageInfo pageInfo) {
        Pageable pageable = PageRequest.of(pageInfo.getPage(), pageInfo.getSize());
        Page<SchoolClassEntity> page = jpaSchoolClassRepository.findAll(pageable);
        
        List<SchoolClass> schoolClasses = page.getContent()
                .stream()
                .map(SchoolClassEntity::toDomain)
                .toList();
        
        return new PageResult<>(schoolClasses, page.getTotalElements(), page.getTotalPages());
    }
    
    @Override
    public PageResult<SchoolClass> findByStatus(ClassStatus status, PageInfo pageInfo) {
        Pageable pageable = PageRequest.of(pageInfo.getPage(), pageInfo.getSize());
        Page<SchoolClassEntity> page = jpaSchoolClassRepository.findByStatus(status, pageable);
        
        List<SchoolClass> schoolClasses = page.getContent()
                .stream()
                .map(SchoolClassEntity::toDomain)
                .toList();
        
        return new PageResult<>(schoolClasses, page.getTotalElements(), page.getTotalPages());
    }
    
    @Override
    public PageResult<SchoolClass> search(String searchTerm, PageInfo pageInfo) {
        Pageable pageable = PageRequest.of(pageInfo.getPage(), pageInfo.getSize());
        Page<SchoolClassEntity> page = jpaSchoolClassRepository.search(searchTerm, pageable);
        
        List<SchoolClass> schoolClasses = page.getContent()
                .stream()
                .map(SchoolClassEntity::toDomain)
                .toList();
        
        return new PageResult<>(schoolClasses, page.getTotalElements(), page.getTotalPages());
    }
    
    @Override
    public boolean existsByCode(String code) {
        return jpaSchoolClassRepository.existsByCode(code);
    }
    
    @Override
    public boolean existsByName(String name, UUID gradeId, UUID academicYearId) {
        return jpaSchoolClassRepository.existsByNameAndGradeIdAndAcademicYearId(name, gradeId, academicYearId);
    }
    
    @Override
    public boolean delete(UUID classId) {
        if (jpaSchoolClassRepository.existsById(classId)) {
            jpaSchoolClassRepository.deleteById(classId);
            return true;
        }
        return false;
    }
    
    @Override
    public long countByGradeId(UUID gradeId) {
        return jpaSchoolClassRepository.countByGradeId(gradeId);
    }
    
    @Override
    public long countByAcademicYearId(UUID academicYearId) {
        return jpaSchoolClassRepository.countByAcademicYearId(academicYearId);
    }
}
