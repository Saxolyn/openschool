package com.openschool.education.academic.port.in;

import com.openschool.education.academic.port.in.command.CreateAcademicYearCommand;
import com.openschool.domain.academic.AcademicYear;

public interface CreateAcademicYearUseCase {
    AcademicYear create(CreateAcademicYearCommand command);
}
