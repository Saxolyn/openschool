package com.openschool.administration.equipment.port.in.command;

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
public class BorrowEquipmentCommand {
    private UUID equipmentId;
    private UUID borrowerId;
    private String borrowerType; // STUDENT, EMPLOYEE, DEPARTMENT
    private Integer quantity;
    private LocalDateTime expectedReturnDate;
    private String purpose;
    private String notes;
}
