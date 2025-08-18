package com.openschool.administration.school.port.in;

import com.openschool.domain.school.School;
import com.openschool.administration.school.port.in.command.CreateSchoolCommand;

public interface CreateSchoolUseCase {
    School create(CreateSchoolCommand command);
}
