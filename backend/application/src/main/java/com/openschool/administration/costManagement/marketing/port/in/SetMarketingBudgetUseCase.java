package com.openschool.administration.costManagement.marketing.port.in;

import com.openschool.administration.costManagement.marketing.port.in.command.SetMarketingBudgetCommand;
import com.openschool.domain.cost.Budget;

public interface SetMarketingBudgetUseCase {
    Budget setMarketingBudget(SetMarketingBudgetCommand command);
}

