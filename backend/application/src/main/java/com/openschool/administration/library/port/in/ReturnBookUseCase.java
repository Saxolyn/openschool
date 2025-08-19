package com.openschool.administration.library.port.in;

import com.openschool.administration.library.port.in.command.ReturnBookCommand;
import com.openschool.domain.library.BookLoan;

public interface ReturnBookUseCase {
    BookLoan returnBook(ReturnBookCommand command);
}
