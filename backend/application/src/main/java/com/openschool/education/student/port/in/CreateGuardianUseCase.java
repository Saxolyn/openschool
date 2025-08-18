package com.openschool.education.student.port.in;

import com.openschool.domain.student.Guardian;
import com.openschool.education.student.port.in.command.CreateGuardianCommand;

public interface CreateGuardianUseCase {
    
    /**
     * Create guardian
     */
    Guardian createGuardian(CreateGuardianCommand command);
}
