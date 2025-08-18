package com.openschool.education.schoolclass.port.in;

import java.util.UUID;

public interface CloseClassUseCase {

    /**
     * Close class (set status to CLOSED)
     */
    void closeClass(UUID classId);
}
