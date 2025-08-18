package com.openschool.education.schoolclass.port.in;

import com.openschool.education.schoolclass.port.in.command.RemoveStudentFromClassCommand;

public interface RemoveStudentFromClassUseCase {

    /**
     * Remove student from class
     */
    void removeStudentFromClass(RemoveStudentFromClassCommand command);
}
