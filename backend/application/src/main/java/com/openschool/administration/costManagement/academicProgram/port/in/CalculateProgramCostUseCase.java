package com.openschool.administration.costManagement.academicProgram.port.in;

import com.openschool.domain.cost.Cost;
import java.util.UUID;

public interface CalculateProgramCostUseCase {
    Cost calculateProgramCost(UUID programId, String fiscalYear);
}
