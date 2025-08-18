package com.openschool.academic.port.in;

import com.openschool.academic.port.in.command.CreateAcademicYearCommand;
import com.openschool.academic.port.in.command.UpdateAcademicYearCommand;
import com.openschool.domain.academic.AcademicYear;

public interface UpdateAcademicYearUseCase {
    AcademicYear update(UpdateAcademicYearCommand command);
}
