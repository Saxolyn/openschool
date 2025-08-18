package com.openschool.education.student.port.in;

import com.openschool.education.student.port.in.command.LinkParentCommand;

public interface LinkParentToStudentUseCase {
    boolean linkParentToStudent(LinkParentCommand command);
}
