package com.openschool.education.academic.port.out;

import com.openschool.common.pageable.PageInfo;
import com.openschool.common.pageable.PageResult;
import com.openschool.domain.academic.AcademicYear;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AcademicYearRepositoryPort {

    AcademicYear create(AcademicYear academicYear);

    AcademicYear update(AcademicYear academicYear);

    Optional<AcademicYear> findById(UUID academicYearId);

    Optional<AcademicYear> findByCode(String code);

    List<AcademicYear> findBySchoolId(UUID schoolId);

    PageResult<AcademicYear> findAll(PageInfo pageInfo);

    PageResult<AcademicYear> findBySchoolId(UUID schoolId, PageInfo pageInfo);

    boolean existsByCode(String code);

    boolean delete(UUID academicYearId);

    long countBySchoolId(UUID schoolId);
}
