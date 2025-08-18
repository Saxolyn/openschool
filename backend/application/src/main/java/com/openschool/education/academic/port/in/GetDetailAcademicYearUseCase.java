package com.openschool.education.academic.port.in;

import com.openschool.common.pageable.PageResult;
import com.openschool.domain.academic.AcademicYear;

import java.util.UUID;

public interface GetDetailAcademicYearUseCase {
    AcademicYear getDetail(UUID academicYearId);
}
