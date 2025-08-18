package com.openschool.student.port.in;

import com.openschool.student.port.in.command.LinkParentCommand;

public interface LinkParentToStudentUseCase {
    boolean linkParentToStudent(LinkParentCommand command);
}
