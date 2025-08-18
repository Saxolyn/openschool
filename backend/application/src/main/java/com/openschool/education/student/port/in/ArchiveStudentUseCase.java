package com.openschool.education.student.port.in;

import java.util.UUID;

public interface ArchiveStudentUseCase {

    /**
     * Archive student (set status to ARCHIVED)
     */
    void archiveStudent(UUID studentId);
}
