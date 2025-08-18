package com.openschool.education.schoolclass.port.in;

import com.openschool.domain.schoolclass.model.SchoolClass;
import com.openschool.education.schoolclass.port.in.command.UpdateClassInfoCommand;

public interface UpdateClassInfoUseCase {

    /**
     * Update class information
     */
    SchoolClass updateClassInfo(UpdateClassInfoCommand command);
}
