package com.openschool.administration.library.port.in;

import com.openschool.domain.library.BookLoan;
import java.util.UUID;

public interface RenewBookLoanUseCase {
    BookLoan renewBookLoan(UUID loanId);
}
