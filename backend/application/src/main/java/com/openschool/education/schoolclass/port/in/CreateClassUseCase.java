package com.openschool.education.schoolclass.port.in;

import com.openschool.domain.schoolclass.model.SchoolClass;
import com.openschool.education.schoolclass.port.in.command.CreateClassCommand;

public interface CreateClassUseCase {
    SchoolClass createClass(CreateClassCommand command);
}
