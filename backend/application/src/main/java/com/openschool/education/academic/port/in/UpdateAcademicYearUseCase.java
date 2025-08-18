package com.openschool.education.academic.port.in;

import com.openschool.education.academic.port.in.command.UpdateAcademicYearCommand;
import com.openschool.domain.academic.AcademicYear;

import java.util.UUID;

public interface UpdateAcademicYearUseCase {
    AcademicYear update(UpdateAcademicYearCommand command, UUID academicYearId);
}
