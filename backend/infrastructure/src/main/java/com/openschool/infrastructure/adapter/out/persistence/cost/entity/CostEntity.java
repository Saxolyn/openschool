package com.openschool.infrastructure.adapter.out.persistence.cost.entity;

import com.openschool.domain.cost.*;
import com.openschool.infrastructure.adapter.out.persistence.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "costs")
public class CostEntity extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    
    @Column(nullable = false)
    private String name;
    
    private String description;
    
    @Enumerated(EnumType.STRING)
    private CostCategory category;
    
    @Enumerated(EnumType.STRING)
    private CostType type;
    
    private Double amount;
    private String currency;
    
    @Enumerated(EnumType.STRING)
    private CostStatus status;
    
    private UUID budgetId;
    private Double budgetedAmount;
    private Double actualAmount;
    private Double variance;
    
    private LocalDateTime periodStart;
    private LocalDateTime periodEnd;
    private String fiscalYear;
    private String quarter;
    private String month;
    
    private UUID schoolId;
    private UUID departmentId;
    private UUID programId;
    private UUID projectId;
    
    @Enumerated(EnumType.STRING)
    private CostApprovalStatus approvalStatus;
    
    private UUID approvedById;
    private LocalDateTime approvedAt;
    private String approvalNotes;
    
    private UUID vendorId;
    private String vendorName;
    private String invoiceNumber;
    private LocalDateTime invoiceDate;
    private LocalDateTime dueDate;
    
    public static CostEntity fromDomain(Cost cost) {
        if (cost == null) return null;
        
        return CostEntity.builder()
                .id(cost.getId())
                .name(cost.getName())
                .description(cost.getDescription())
                .category(cost.getCategory())
                .type(cost.getType())
                .amount(cost.getAmount())
                .currency(cost.getCurrency())
                .status(cost.getStatus())
                .budgetId(cost.getBudgetId())
                .budgetedAmount(cost.getBudgetedAmount())
                .actualAmount(cost.getActualAmount())
                .variance(cost.getVariance())
                .periodStart(cost.getPeriodStart())
                .periodEnd(cost.getPeriodEnd())
                .fiscalYear(cost.getFiscalYear())
                .quarter(cost.getQuarter())
                .month(cost.getMonth())
                .schoolId(cost.getSchoolId())
                .departmentId(cost.getDepartmentId())
                .programId(cost.getProgramId())
                .projectId(cost.getProjectId())
                .approvalStatus(cost.getApprovalStatus())
                .approvedById(cost.getApprovedById())
                .approvedAt(cost.getApprovedAt())
                .approvalNotes(cost.getApprovalNotes())
                .vendorId(cost.getVendorId())
                .vendorName(cost.getVendorName())
                .invoiceNumber(cost.getInvoiceNumber())
                .invoiceDate(cost.getInvoiceDate())
                .dueDate(cost.getDueDate())
                .build();
    }
    
    public Cost toDomain() {
        return Cost.builder()
                .id(this.id)
                .name(this.name)
                .description(this.description)
                .category(this.category)
                .type(this.type)
                .amount(this.amount)
                .currency(this.currency)
                .status(this.status)
                .budgetId(this.budgetId)
                .budgetedAmount(this.budgetedAmount)
                .actualAmount(this.actualAmount)
                .variance(this.variance)
                .periodStart(this.periodStart)
                .periodEnd(this.periodEnd)
                .fiscalYear(this.fiscalYear)
                .quarter(this.quarter)
                .month(this.month)
                .schoolId(this.schoolId)
                .departmentId(this.departmentId)
                .programId(this.programId)
                .projectId(this.projectId)
                .approvalStatus(this.approvalStatus)
                .approvedById(this.approvedById)
                .approvedAt(this.approvedAt)
                .approvalNotes(this.approvalNotes)
                .vendorId(this.vendorId)
                .vendorName(this.vendorName)
                .invoiceNumber(this.invoiceNumber)
                .invoiceDate(this.invoiceDate)
                .dueDate(this.dueDate)
                .createdAt(this.getCreatedAt())
                .updatedAt(this.getUpdatedAt())
                .build();
    }
}
