package com.openschool.administration.library.port.in;

import com.openschool.administration.library.port.in.command.BorrowBookCommand;
import com.openschool.domain.library.BookLoan;

public interface BorrowBookUseCase {
    BookLoan borrowBook(BorrowBookCommand command);
}
