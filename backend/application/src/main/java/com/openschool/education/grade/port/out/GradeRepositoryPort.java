package com.openschool.education.grade.port.out;

import com.openschool.common.pageable.PageInfo;
import com.openschool.common.pageable.PageResult;
import com.openschool.domain.grade.Grade;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GradeRepositoryPort {

    Grade createGrade(Grade grade);

    Grade update(Grade grade);

    Optional<Grade> findById(UUID gradeId);

    Optional<Grade> findByCode(String code);

    List<Grade> findBySchoolId(UUID schoolId);

    PageResult<Grade> findAll(PageInfo pageInfo);

    PageResult<Grade> findBySchoolId(UUID schoolId, PageInfo pageInfo);

    boolean existsByCode(String code);

    boolean existsByCodeAndSchoolId(String code, UUID schoolId);

    boolean delete(UUID gradeId);

    long countBySchoolId(UUID schoolId);
}
