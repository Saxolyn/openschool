package com.openschool.education.student.port.in;

import com.openschool.education.student.port.in.command.TransferStudentCommand;

public interface TransferStudentUseCase {

    /**
     * Transfer student to another school/grade/class
     */
    void transferStudent(TransferStudentCommand command);
}
