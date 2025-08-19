package com.openschool.administration.costManagement.operations.port.in;

import com.openschool.administration.costManagement.operations.port.in.command.ManageMaintenanceBudgetCommand;
import com.openschool.domain.cost.Budget;

public interface ManageMaintenanceBudgetUseCase {
    Budget manageMaintenanceBudget(ManageMaintenanceBudgetCommand command);
}

