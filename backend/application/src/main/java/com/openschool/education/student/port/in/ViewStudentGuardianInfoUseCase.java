package com.openschool.education.student.port.in;

import com.openschool.domain.student.Guardian;

import java.util.List;
import java.util.UUID;

public interface ViewStudentGuardianInfoUseCase {

    /**
     * View student guardian information
     */
    List<Guardian> viewStudentGuardianInfo(UUID studentId);
}
