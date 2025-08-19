package com.openschool.administration.library.port.in;

import com.openschool.administration.library.port.in.command.UpdateBookCommand;
import com.openschool.domain.library.Book;

public interface UpdateBookUseCase {
    Book updateBook(UpdateBookCommand command);
}
