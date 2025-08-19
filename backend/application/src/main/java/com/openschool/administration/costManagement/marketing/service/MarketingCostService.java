package com.openschool.administration.costManagement.marketing.service;

import com.openschool.administration.costManagement.marketing.port.in.*;
import com.openschool.administration.costManagement.marketing.port.in.command.*;
import com.openschool.administration.costManagement.academicProgram.port.out.*;
import com.openschool.common.pageable.PageInfo;
import com.openschool.common.pageable.PageResult;
import com.openschool.domain.cost.*;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
public class MarketingCostService implements
        SetMarketingBudgetUseCase,
        TrackMarketingCampaignCostUseCase {

    private final CostRepositoryPort costRepository;
    private final BudgetRepositoryPort budgetRepository;

    @Override
    public Budget setMarketingBudget(SetMarketingBudgetCommand command) {
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
                .category(CostCategory.MARKETING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return budgetRepository.create(budget);
    }

    @Override
    public PageResult<Cost> trackMarketingCampaignCost(UUID campaignId, PageInfo pageInfo) {
        return costRepository.findByProjectId(campaignId, pageInfo);
    }
}
