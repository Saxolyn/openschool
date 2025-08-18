package com.openschool.education.student.port.in;

import com.openschool.domain.student.Guardian;
import com.openschool.education.student.port.in.command.UpdateGuardianCommand;

public interface UpdateGuardianUseCase {
    
    /**
     * Update guardian information
     */
    Guardian updateGuardian(UpdateGuardianCommand command);
}
