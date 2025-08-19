package com.openschool.administration.tuitionFee.service;

import com.openschool.administration.tuitionFee.port.in.command.GenerateTuitionInvoiceCommand;
import com.openschool.administration.tuitionFee.port.out.TuitionFeeRepositoryPort;
import com.openschool.administration.tuitionFee.port.out.PaymentRepositoryPort;
import com.openschool.domain.tuition.TuitionFee;
import com.openschool.domain.tuition.TuitionFeeStatus;
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
class TuitionFeeServiceTest {

    @Mock
    private TuitionFeeRepositoryPort tuitionFeeRepository;

    @Mock
    private PaymentRepositoryPort paymentRepository;

    @InjectMocks
    private TuitionFeeService tuitionFeeService;

    private GenerateTuitionInvoiceCommand command;
    private TuitionFee tuitionFee;

    @BeforeEach
    void setUp() {
        command = GenerateTuitionInvoiceCommand.builder()
                .studentId(UUID.randomUUID())
                .academicYearId(UUID.randomUUID())
                .gradeId(UUID.randomUUID())
                .feeType("TUITION")
                .description("Monthly tuition fee")
                .baseAmount(1000.0)
                .dueDate(LocalDateTime.now().plusDays(30))
                .schoolId(UUID.randomUUID())
                .build();

        tuitionFee = TuitionFee.builder()
                .id(UUID.randomUUID())
                .studentId(command.getStudentId())
                .baseAmount(1000.0)
                .finalAmount(1000.0)
                .outstandingAmount(1000.0)
                .status(TuitionFeeStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    void generateTuitionInvoice_Success() {
        when(tuitionFeeRepository.create(any(TuitionFee.class))).thenReturn(tuitionFee);

        TuitionFee result = tuitionFeeService.generateTuitionInvoice(command);

        assertNotNull(result);
        assertEquals(1000.0, result.getBaseAmount());
        assertEquals(TuitionFeeStatus.PENDING, result.getStatus());
        verify(tuitionFeeRepository).create(any(TuitionFee.class));
    }
}
