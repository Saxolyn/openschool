package com.openschool.administration.costManagement.admissions.port.in.command;

import com.openschool.domain.cost.BudgetType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SetAdmissionsMarketingBudgetCommand {
    private String name;
    private String description;
    private BudgetType type;
    private Double totalAmount;
    private String currency;
    private LocalDateTime periodStart;
    private LocalDateTime periodEnd;
    private String fiscalYear;
    private UUID schoolId;
    private UUID departmentId;
}
