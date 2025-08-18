package com.openschool.administration.school.port.in;

import com.openschool.domain.school.School;
import com.openschool.administration.school.port.in.command.UpdateSchoolCommand;

public interface UpdateSchoolUseCase {
    School update(UpdateSchoolCommand command);
}
