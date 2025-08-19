package com.openschool.administration.costManagement.admissions.port.in;

import com.openschool.domain.cost.Cost;
import java.util.UUID;

public interface CalculateTotalAdmissionsCostUseCase {
    Cost calculateTotalAdmissionsCost(UUID schoolId, String fiscalYear);
}

