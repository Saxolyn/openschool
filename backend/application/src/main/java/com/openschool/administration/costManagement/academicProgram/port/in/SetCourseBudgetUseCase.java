package com.openschool.administration.costManagement.academicProgram.port.in;

import com.openschool.administration.costManagement.academicProgram.port.in.command.SetCourseBudgetCommand;
import com.openschool.domain.cost.Budget;

public interface SetCourseBudgetUseCase {
    Budget setCourseBudget(SetCourseBudgetCommand command);
}
