package com.openschool.education.student.port.in;

import com.openschool.domain.student.Student;
import com.openschool.education.student.port.in.command.CreateStudentCommand;

public interface CreateStudentProfileUseCase {
    Student createStudent(CreateStudentCommand command);
}
