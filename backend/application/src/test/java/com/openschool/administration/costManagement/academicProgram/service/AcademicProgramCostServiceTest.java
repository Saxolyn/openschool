package com.openschool.administration.costManagement.academicProgram.service;

import com.openschool.administration.costManagement.academicProgram.port.in.command.SetCourseBudgetCommand;
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
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AcademicProgramCostServiceTest {

    @Mock
    private CostRepositoryPort costRepository;

    @Mock
    private BudgetRepositoryPort budgetRepository;

    @InjectMocks
    private AcademicProgramCostService academicProgramCostService;

    private UUID programId;
    private String fiscalYear;
    private SetCourseBudgetCommand budgetCommand;

    @BeforeEach
    void setUp() {
        programId = UUID.randomUUID();
        fiscalYear = "2024";
        
        budgetCommand = SetCourseBudgetCommand.builder()
                .courseId(programId)
                .name("Course Budget 2024")
                .description("Annual budget for course")
                .type(BudgetType.ANNUAL)
                .totalAmount(50000.0)
                .currency("USD")
                .fiscalYear(fiscalYear)
                .schoolId(UUID.randomUUID())
                .departmentId(UUID.randomUUID())
                .build();
    }

    @Test
    void calculateProgramCost_Success() {
        // Given
        List<Cost> mockCosts = Arrays.asList(
                Cost.builder().amount(10000.0).build(),
                Cost.builder().amount(15000.0).build(),
                Cost.builder().amount(20000.0).build()
        );
        
        when(costRepository.findByProgramIdAndFiscalYear(programId, fiscalYear))
                .thenReturn(mockCosts);

        // When
        Cost result = academicProgramCostService.calculateProgramCost(programId, fiscalYear);

        // Then
        assertNotNull(result);
        assertEquals(45000.0, result.getAmount());
        assertEquals(CostCategory.ACADEMIC_PROGRAM, result.getCategory());
        assertEquals(CostType.DIRECT, result.getType());
        verify(costRepository).findByProgramIdAndFiscalYear(programId, fiscalYear);
    }

    @Test
    void setCourseBudget_Success() {
        // Given
        Budget mockBudget = Budget.builder()
                .id(UUID.randomUUID())
                .name(budgetCommand.getName())
                .totalAmount(budgetCommand.getTotalAmount())
                .status(BudgetStatus.DRAFT)
                .build();
        
        when(budgetRepository.create(any(Budget.class))).thenReturn(mockBudget);

        // When
        Budget result = academicProgramCostService.setCourseBudget(budgetCommand);

        // Then
        assertNotNull(result);
        assertEquals(budgetCommand.getName(), result.getName());
        assertEquals(budgetCommand.getTotalAmount(), result.getTotalAmount());
        assertEquals(BudgetStatus.DRAFT, result.getStatus());
        verify(budgetRepository).create(any(Budget.class));
    }

    @Test
    void calculateProgramCost_EmptyList_ReturnsZero() {
        // Given
        when(costRepository.findByProgramIdAndFiscalYear(programId, fiscalYear))
                .thenReturn(Arrays.asList());

        // When
        Cost result = academicProgramCostService.calculateProgramCost(programId, fiscalYear);

        // Then
        assertNotNull(result);
        assertEquals(0.0, result.getAmount());
    }
}
