package com.openschool.administration.library.port.in.command;

import com.openschool.domain.library.BookCondition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReturnBookCommand {
    private UUID loanId;
    private BookCondition returnCondition;
    private String returnNotes;
}
