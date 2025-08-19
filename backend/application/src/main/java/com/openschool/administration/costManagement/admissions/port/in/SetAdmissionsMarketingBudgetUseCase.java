package com.openschool.administration.costManagement.admissions.port.in;

import com.openschool.administration.costManagement.admissions.port.in.command.SetAdmissionsMarketingBudgetCommand;
import com.openschool.domain.cost.Budget;

public interface SetAdmissionsMarketingBudgetUseCase {
    Budget setAdmissionsMarketingBudget(SetAdmissionsMarketingBudgetCommand command);
}
