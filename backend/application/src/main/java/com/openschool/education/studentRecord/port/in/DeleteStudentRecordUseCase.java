package com.openschool.education.studentRecord.port.in;

import java.util.UUID;

public interface DeleteStudentRecordUseCase {
    boolean deleteStudentRecord(UUID recordId);
}

