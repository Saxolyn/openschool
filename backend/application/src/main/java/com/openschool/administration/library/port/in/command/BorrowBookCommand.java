package com.openschool.administration.library.port.in.command;

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
public class BorrowBookCommand {
    private UUID bookId;
    private UUID borrowerId;
    private String borrowerType;
    private LocalDateTime dueDate;
    private String notes;
}
