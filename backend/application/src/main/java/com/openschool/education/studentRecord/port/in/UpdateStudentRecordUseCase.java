package com.openschool.education.studentRecord.port.in;

import com.openschool.education.studentRecord.port.in.command.UpdateStudentRecordCommand;
import com.openschool.domain.studentrecord.StudentRecord;

public interface UpdateStudentRecordUseCase {
    StudentRecord updateStudentRecord(UpdateStudentRecordCommand command);
}

