package com.openschool.administration.library.port.in;

import java.util.UUID;

public interface DeleteBookUseCase {
    void deleteBook(UUID bookId);
}
