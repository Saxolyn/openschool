package com.openschool.education.studentRecord.port.in;

import com.openschool.common.pageable.PageInfo;
import com.openschool.common.pageable.PageResult;
import com.openschool.domain.studentrecord.StudentRecord;
import java.util.UUID;

public interface ViewStudentRecordUseCase {
    StudentRecord viewStudentRecord(UUID recordId);
    PageResult<StudentRecord> viewStudentRecordsByStudent(UUID studentId, PageInfo pageInfo);
}

