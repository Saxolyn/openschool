package com.openschool.administration.tuitionFee.port.in.command;

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
public class GenerateTuitionInvoiceCommand {
    private UUID studentId;
    private UUID academicYearId;
    private UUID gradeId;
    private String feeType;
    private String description;
    private Double baseAmount;
    private LocalDateTime dueDate;
    private UUID schoolId;
}
