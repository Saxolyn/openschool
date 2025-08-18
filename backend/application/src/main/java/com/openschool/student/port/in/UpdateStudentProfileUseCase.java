package com.openschool.student.port.in;

import com.openschool.domain.student.Student;
import com.openschool.student.port.in.command.UpdateStudentCommand;

public interface UpdateStudentProfileUseCase {
    Student updateStudent(UpdateStudentCommand command);
}
