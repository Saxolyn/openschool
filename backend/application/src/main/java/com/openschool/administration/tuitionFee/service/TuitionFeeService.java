package com.openschool.administration.tuitionFee.service;

import com.openschool.administration.tuitionFee.port.in.*;
import com.openschool.administration.tuitionFee.port.in.command.*;
import com.openschool.administration.tuitionFee.port.out.*;
import com.openschool.domain.tuition.*;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
public class TuitionFeeService implements
        GenerateTuitionInvoiceUseCase,
        ProcessTuitionPaymentUseCase {

    private final TuitionFeeRepositoryPort tuitionFeeRepository;
    private final PaymentRepositoryPort paymentRepository;

    @Override
    public TuitionFee generateTuitionInvoice(GenerateTuitionInvoiceCommand command) {
        TuitionFee tuitionFee = TuitionFee.builder()
                .id(UUID.randomUUID())
                .studentId(command.getStudentId())
                .academicYearId(command.getAcademicYearId())
                .gradeId(command.getGradeId())
                .feeType(command.getFeeType())
                .description(command.getDescription())
                .baseAmount(command.getBaseAmount())
                .discountAmount(0.0)
                .scholarshipAmount(0.0)
                .finalAmount(command.getBaseAmount())
                .paidAmount(0.0)
                .outstandingAmount(command.getBaseAmount())
                .status(TuitionFeeStatus.PENDING)
                .dueDate(command.getDueDate())
                .schoolId(command.getSchoolId())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return tuitionFeeRepository.create(tuitionFee);
    }

    @Override
    public Payment processTuitionPayment(ProcessTuitionPaymentCommand command) {
        Payment payment = Payment.builder()
                .id(UUID.randomUUID())
                .tuitionFeeId(command.getTuitionFeeId())
                .studentId(command.getStudentId())
                .amount(command.getAmount())
                .method(command.getMethod())
                .status(PaymentStatus.COMPLETED)
                .transactionId(command.getTransactionId())
                .reference(command.getReference())
                .notes(command.getNotes())
                .paymentDate(LocalDateTime.now())
                .processedDate(LocalDateTime.now())
                .processedById(command.getProcessedById())
                .receiptNumber(generateReceiptNumber())
                .refundAmount(null)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return paymentRepository.create(payment);
    }

    private String generateReceiptNumber() {
        return "RCP" + System.currentTimeMillis();
    }
}
