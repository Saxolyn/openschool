package com.openschool.academic.port.in;

import com.openschool.domain.academic.AcademicYear;

import java.util.UUID;

public interface DeleteAcademicYearUseCase {
    void delete(UUID academicYearId);
}
