package com.openschool.domain.tuition;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class Payment {
    private UUID id;
    private UUID tuitionFeeId;
    private UUID studentId;
    
    // Payment details
    private Double amount;
    private PaymentMethod method;
    private PaymentStatus status;
    private String transactionId;
    private String reference;
    private String notes;
    
    // Payment processing
    private LocalDateTime paymentDate;
    private LocalDateTime processedDate;
    private UUID processedById;
    
    // Receipt information
    private String receiptNumber;
    private String receiptUrl;
    
    // Refund information
    private Double refundAmount;
    private LocalDateTime refundDate;
    private String refundReason;
    
    // Audit fields
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public boolean isSuccessful() {
        return status == PaymentStatus.COMPLETED;
    }
    
    public boolean canBeRefunded() {
        return status == PaymentStatus.COMPLETED && refundAmount == null;
    }
    
    public void processPayment(UUID processedById) {
        this.status = PaymentStatus.COMPLETED;
        this.processedDate = LocalDateTime.now();
        this.processedById = processedById;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void refundPayment(Double refundAmount, String reason) {
        if (!canBeRefunded()) {
            throw new IllegalStateException("Payment cannot be refunded");
        }
        
        if (refundAmount > amount) {
            throw new IllegalArgumentException("Refund amount cannot exceed payment amount");
        }
        
        this.refundAmount = refundAmount;
        this.refundDate = LocalDateTime.now();
        this.refundReason = reason;
        this.status = PaymentStatus.REFUNDED;
        this.updatedAt = LocalDateTime.now();
    }
}
