package com.openschool.administration.tuitionFee.port.in.command;

import com.openschool.domain.tuition.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProcessTuitionPaymentCommand {
    private UUID tuitionFeeId;
    private UUID studentId;
    private Double amount;
    private PaymentMethod method;
    private String transactionId;
    private String reference;
    private String notes;
    private UUID processedById;
}
