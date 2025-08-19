package com.openschool.administration.library.port.in;

import com.openschool.domain.library.Book;
import java.util.UUID;

public interface GetDetailBookUseCase {
    Book getDetailBook(UUID bookId);
}
