package com.openschool.administration.library.port.in;

import com.openschool.administration.library.port.in.command.CreateBookCommand;
import com.openschool.domain.library.Book;

public interface CreateBookUseCase {
    Book createBook(CreateBookCommand command);
}
