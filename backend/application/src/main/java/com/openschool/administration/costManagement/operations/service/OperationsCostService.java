package com.openschool.administration.costManagement.operations.service;

import com.openschool.administration.costManagement.operations.port.in.*;
import com.openschool.administration.costManagement.operations.port.in.command.*;
import com.openschool.administration.costManagement.academicProgram.port.out.*;
import com.openschool.domain.cost.*;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
public class OperationsCostService implements ManageMaintenanceBudgetUseCase {

    private final BudgetRepositoryPort budgetRepository;

    @Override
    public Budget manageMaintenanceBudget(ManageMaintenanceBudgetCommand command) {
        Budget budget = Budget.builder()
                .id(UUID.randomUUID())
                .name(command.getName())
                .description(command.getDescription())
                .type(command.getType())
                .status(BudgetStatus.DRAFT)
                .totalAmount(command.getTotalAmount())
                .allocatedAmount(0.0)
                .spentAmount(0.0)
                .remainingAmount(command.getTotalAmount())
                .currency(command.getCurrency())
                .periodStart(command.getPeriodStart())
                .periodEnd(command.getPeriodEnd())
                .fiscalYear(command.getFiscalYear())
                .schoolId(command.getSchoolId())
                .departmentId(command.getDepartmentId())
                .category(CostCategory.OPERATIONS)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return budgetRepository.create(budget);
    }
}
