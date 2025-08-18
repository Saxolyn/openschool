package com.openschool.education.schoolclass.port.in;

import com.openschool.education.schoolclass.port.in.command.EnrollStudentCommand;

public interface EnrollStudentToClassUseCase {
    boolean enrollStudent(EnrollStudentCommand command);
}
