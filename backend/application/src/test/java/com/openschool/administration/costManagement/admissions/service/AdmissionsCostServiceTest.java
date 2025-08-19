package com.openschool.administration.costManagement.admissions.service;

import com.openschool.administration.costManagement.admissions.port.in.command.SetAdmissionsMarketingBudgetCommand;
import com.openschool.administration.costManagement.academicProgram.port.out.BudgetRepositoryPort;
import com.openschool.administration.costManagement.academicProgram.port.out.CostRepositoryPort;
import com.openschool.domain.cost.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdmissionsCostServiceTest {

    @Mock
    private CostRepositoryPort costRepository;

    @Mock
    private BudgetRepositoryPort budgetRepository;

    @InjectMocks
    private AdmissionsCostService admissionsCostService;

    private UUID schoolId;
    private String fiscalYear;
    private SetAdmissionsMarketingBudgetCommand budgetCommand;

    @BeforeEach
    void setUp() {
        schoolId = UUID.randomUUID();
        fiscalYear = "2024";
        
        budgetCommand = SetAdmissionsMarketingBudgetCommand.builder()
                .name("Admissions Marketing Budget 2024")
                .description("Annual marketing budget for admissions")
                .type(BudgetType.ANNUAL)
                .totalAmount(100000.0)
                .currency("USD")
                .fiscalYear(fiscalYear)
                .schoolId(schoolId)
                .departmentId(UUID.randomUUID())
                .build();
    }

    @Test
    void calculateTotalAdmissionsCost_Success() {
        // Given
        Double expectedCost = 75000.0;
        when(costRepository.calculateTotalCostBySchoolAndCategory(schoolId, CostCategory.ADMISSIONS, fiscalYear))
                .thenReturn(expectedCost);

        // When
        Cost result = admissionsCostService.calculateTotalAdmissionsCost(schoolId, fiscalYear);

        // Then
        assertNotNull(result);
        assertEquals(expectedCost, result.getAmount());
        assertEquals(CostCategory.ADMISSIONS, result.getCategory());
        assertEquals(CostType.DIRECT, result.getType());
        assertEquals(schoolId, result.getSchoolId());
        assertEquals(fiscalYear, result.getFiscalYear());
        verify(costRepository).calculateTotalCostBySchoolAndCategory(schoolId, CostCategory.ADMISSIONS, fiscalYear);
    }

    @Test
    void calculateTotalAdmissionsCost_NullResult_ReturnsZero() {
        // Given
        when(costRepository.calculateTotalCostBySchoolAndCategory(schoolId, CostCategory.ADMISSIONS, fiscalYear))
                .thenReturn(null);

        // When
        Cost result = admissionsCostService.calculateTotalAdmissionsCost(schoolId, fiscalYear);

        // Then
        assertNotNull(result);
        assertEquals(0.0, result.getAmount());
    }

    @Test
    void setAdmissionsMarketingBudget_Success() {
        // Given
        Budget mockBudget = Budget.builder()
                .id(UUID.randomUUID())
                .name(budgetCommand.getName())
                .totalAmount(budgetCommand.getTotalAmount())
                .status(BudgetStatus.DRAFT)
                .category(CostCategory.ADMISSIONS)
                .build();
        
        when(budgetRepository.create(any(Budget.class))).thenReturn(mockBudget);

        // When
        Budget result = admissionsCostService.setAdmissionsMarketingBudget(budgetCommand);

        // Then
        assertNotNull(result);
        assertEquals(budgetCommand.getName(), result.getName());
        assertEquals(budgetCommand.getTotalAmount(), result.getTotalAmount());
        assertEquals(BudgetStatus.DRAFT, result.getStatus());
        assertEquals(CostCategory.ADMISSIONS, result.getCategory());
        verify(budgetRepository).create(any(Budget.class));
    }
}
