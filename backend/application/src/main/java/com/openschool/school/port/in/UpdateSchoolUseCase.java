package com.openschool.school.port.in;

import com.openschool.domain.school.School;
import com.openschool.school.port.in.command.UpdateSchoolCommand;

import java.util.UUID;

public interface UpdateSchoolUseCase {
    School update(UpdateSchoolCommand command);
}
