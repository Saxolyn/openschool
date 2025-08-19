package com.openschool.administration.library.service;

import com.openschool.administration.library.exception.LibraryException;
import com.openschool.administration.library.port.in.command.CreateBookCommand;
import com.openschool.administration.library.port.out.BookRepositoryPort;
import com.openschool.administration.library.port.out.BookLoanRepositoryPort;
import com.openschool.administration.library.port.out.LibraryUserRepositoryPort;
import com.openschool.domain.library.Book;
import com.openschool.domain.library.BookCondition;
import com.openschool.domain.library.BookStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LibraryServiceTest {

    @Mock
    private BookRepositoryPort bookRepository;

    @Mock
    private BookLoanRepositoryPort bookLoanRepository;

    @Mock
    private LibraryUserRepositoryPort libraryUserRepository;

    @InjectMocks
    private LibraryService libraryService;

    private CreateBookCommand createCommand;
    private Book book;

    @BeforeEach
    void setUp() {
        createCommand = CreateBookCommand.builder()
                .title("Test Book")
                .isbn("978-0123456789")
                .author("Test Author")
                .publisher("Test Publisher")
                .category("Fiction")
                .totalCopies(5)
                .status(BookStatus.ACTIVE)
                .condition(BookCondition.GOOD)
                .build();

        book = Book.builder()
                .id(UUID.randomUUID())
                .title("Test Book")
                .isbn("978-0123456789")
                .author("Test Author")
                .totalCopies(5)
                .availableCopies(5)
                .borrowedCopies(0)
                .status(BookStatus.ACTIVE)
                .condition(BookCondition.GOOD)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void createBook_Success() {
        when(bookRepository.existsByIsbn(anyString())).thenReturn(false);
        when(bookRepository.create(any(Book.class))).thenReturn(book);

        Book result = libraryService.createBook(createCommand);

        assertNotNull(result);
        assertEquals("Test Book", result.getTitle());
        verify(bookRepository).create(any(Book.class));
    }

    @Test
    void createBook_DuplicateISBN_ThrowsException() {
        when(bookRepository.existsByIsbn(anyString())).thenReturn(true);

        assertThrows(LibraryException.class, () -> libraryService.createBook(createCommand));
    }

    @Test
    void getDetailBook_Success() {
        UUID bookId = UUID.randomUUID();
        when(bookRepository.findById(bookId)).thenReturn(java.util.Optional.of(book));

        Book result = libraryService.getDetailBook(bookId);

        assertNotNull(result);
        assertEquals("Test Book", result.getTitle());
    }
}
