package com.openschool.education.schoolclass.port.in;

import com.openschool.education.schoolclass.port.in.command.TransferStudentBetweenClassesCommand;

public interface TransferStudentBetweenClassesUseCase {

    /**
     * Transfer student between classes
     */
    void transferStudentBetweenClasses(TransferStudentBetweenClassesCommand command);
}
