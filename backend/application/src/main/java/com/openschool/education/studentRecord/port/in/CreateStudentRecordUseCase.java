package com.openschool.education.studentRecord.port.in;

import com.openschool.education.studentRecord.port.in.command.CreateStudentRecordCommand;
import com.openschool.domain.studentrecord.StudentRecord;

public interface CreateStudentRecordUseCase {
    StudentRecord createStudentRecord(CreateStudentRecordCommand command);
}

