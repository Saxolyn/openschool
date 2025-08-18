package com.openschool.schoolclass.port.in;

import com.openschool.schoolclass.port.in.command.EnrollStudentCommand;

public interface EnrollStudentToClassUseCase {
    boolean enrollStudent(EnrollStudentCommand command);
}
