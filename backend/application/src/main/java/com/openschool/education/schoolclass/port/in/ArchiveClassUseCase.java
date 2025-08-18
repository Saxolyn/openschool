package com.openschool.education.schoolclass.port.in;

import java.util.UUID;

public interface ArchiveClassUseCase {

    /**
     * Archive class (set status to ARCHIVED)
     */
    void archiveClass(UUID classId);
}
