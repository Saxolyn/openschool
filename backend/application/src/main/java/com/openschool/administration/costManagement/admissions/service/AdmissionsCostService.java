package com.openschool.administration.costManagement.admissions.service;

import com.openschool.administration.costManagement.admissions.port.in.*;
import com.openschool.administration.costManagement.admissions.port.in.command.*;
import com.openschool.administration.costManagement.academicProgram.port.out.*;
import com.openschool.domain.cost.*;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
public class AdmissionsCostService implements
        CalculateTotalAdmissionsCostUseCase,
        SetAdmissionsMarketingBudgetUseCase {

    private final CostRepositoryPort costRepository;
    private final BudgetRepositoryPort budgetRepository;

    @Override
    public Cost calculateTotalAdmissionsCost(UUID schoolId, String fiscalYear) {
        Double totalCost = costRepository.calculateTotalCostBySchoolAndCategory(schoolId, CostCategory.ADMISSIONS, fiscalYear);
        
        return Cost.builder()
                .id(UUID.randomUUID())
                .name("Total Admissions Cost - " + fiscalYear)
                .description("Calculated total admissions cost for school")
                .category(CostCategory.ADMISSIONS)
                .type(CostType.DIRECT)
                .amount(totalCost != null ? totalCost : 0.0)
                .currency("USD")
                .status(CostStatus.INCURRED)
                .schoolId(schoolId)
                .fiscalYear(fiscalYear)
                .approvalStatus(CostApprovalStatus.APPROVED)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Override
    public Budget setAdmissionsMarketingBudget(SetAdmissionsMarketingBudgetCommand command) {
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
                .category(CostCategory.ADMISSIONS)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return budgetRepository.create(budget);
    }
}
