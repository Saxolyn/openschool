package com.openschool.administration.costManagement.academicProgram.service;

import com.openschool.administration.costManagement.academicProgram.port.in.*;
import com.openschool.administration.costManagement.academicProgram.port.in.command.*;
import com.openschool.administration.costManagement.academicProgram.port.out.*;
import com.openschool.domain.cost.*;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
public class AcademicProgramCostService implements
        CalculateProgramCostUseCase,
        SetCourseBudgetUseCase {

    private final CostRepositoryPort costRepository;
    private final BudgetRepositoryPort budgetRepository;

    @Override
    public Cost calculateProgramCost(UUID programId, String fiscalYear) {
        List<Cost> programCosts = costRepository.findByProgramIdAndFiscalYear(programId, fiscalYear);
        
        Double totalAmount = programCosts.stream()
                .mapToDouble(Cost::getAmount)
                .sum();
        
        return Cost.builder()
                .id(UUID.randomUUID())
                .name("Program Total Cost - " + fiscalYear)
                .description("Calculated total cost for program")
                .category(CostCategory.ACADEMIC_PROGRAM)
                .type(CostType.DIRECT)
                .amount(totalAmount)
                .currency("USD")
                .status(CostStatus.INCURRED)
                .programId(programId)
                .fiscalYear(fiscalYear)
                .approvalStatus(CostApprovalStatus.APPROVED)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Override
    public Budget setCourseBudget(SetCourseBudgetCommand command) {
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
                .programId(command.getCourseId())
                .category(CostCategory.ACADEMIC_PROGRAM)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return budgetRepository.create(budget);
    }
}
