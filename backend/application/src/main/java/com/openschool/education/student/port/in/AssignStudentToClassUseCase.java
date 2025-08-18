package com.openschool.education.student.port.in;

import com.openschool.education.student.port.in.command.AssignStudentToClassCommand;

public interface AssignStudentToClassUseCase {

    /**
     * Assign student to a class
     */
    void assignStudentToClass(AssignStudentToClassCommand command);
}
