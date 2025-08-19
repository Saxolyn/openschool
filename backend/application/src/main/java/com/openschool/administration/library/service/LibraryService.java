package com.openschool.administration.library.service;

import com.openschool.administration.library.exception.LibraryException;
import com.openschool.administration.library.port.in.*;
import com.openschool.administration.library.port.in.command.*;
import com.openschool.administration.library.port.out.BookLoanRepositoryPort;
import com.openschool.administration.library.port.out.BookRepositoryPort;
import com.openschool.administration.library.port.out.LibraryUserRepositoryPort;
import com.openschool.common.pageable.PageInfo;
import com.openschool.common.pageable.PageResult;
import com.openschool.domain.library.*;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
public class LibraryService implements
        CreateBookUseCase,
        UpdateBookUseCase,
        DeleteBookUseCase,
        GetBookListUseCase,
        GetDetailBookUseCase,
        BorrowBookUseCase,
        ReturnBookUseCase,
        RenewBookLoanUseCase,
        RegisterLibraryUserUseCase,
        DeactivateLibraryAccountUseCase,
        SendOverdueNoticeUseCase,
        TrackOverdueBooksUseCase,
        ViewUserBorrowingHistoryUseCase {

    private final BookRepositoryPort bookRepository;
    private final BookLoanRepositoryPort bookLoanRepository;
    private final LibraryUserRepositoryPort libraryUserRepository;

    @Override
    public Book createBook(CreateBookCommand command) {
        if (bookRepository.existsByIsbn(command.getIsbn())) {
            throw new LibraryException("Book with ISBN " + command.getIsbn() + " already exists");
        }

        Book book = Book.builder()
                .id(UUID.randomUUID())
                .title(command.getTitle())
                .isbn(command.getIsbn())
                .author(command.getAuthor())
                .publisher(command.getPublisher())
                .category(command.getCategory())
                .description(command.getDescription())
                .totalCopies(command.getTotalCopies() != null ? command.getTotalCopies() : 1)
                .availableCopies(command.getTotalCopies() != null ? command.getTotalCopies() : 1)
                .borrowedCopies(0)
                .location(command.getLocation())
                .shelfNumber(command.getShelfNumber())
                .publicationYear(command.getPublicationYear())
                .language(command.getLanguage())
                .pages(command.getPages())
                .status(command.getStatus() != null ? command.getStatus() : BookStatus.ACTIVE)
                .condition(command.getCondition() != null ? command.getCondition() : BookCondition.GOOD)
                .schoolId(command.getSchoolId())
                .libraryId(command.getLibraryId())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return bookRepository.create(book);
    }

    @Override
    public Book updateBook(UpdateBookCommand command) {
        Book existingBook = bookRepository.findById(command.getId())
                .orElseThrow(() -> new LibraryException("Book not found with id: " + command.getId()));

        if (!existingBook.getIsbn().equals(command.getIsbn()) && 
            bookRepository.existsByIsbn(command.getIsbn())) {
            throw new LibraryException("Book with ISBN " + command.getIsbn() + " already exists");
        }

        Book updatedBook = Book.builder()
                .id(existingBook.getId())
                .title(command.getTitle())
                .isbn(command.getIsbn())
                .author(command.getAuthor())
                .publisher(command.getPublisher())
                .category(command.getCategory())
                .description(command.getDescription())
                .totalCopies(existingBook.getTotalCopies())
                .availableCopies(existingBook.getAvailableCopies())
                .borrowedCopies(existingBook.getBorrowedCopies())
                .location(command.getLocation())
                .shelfNumber(command.getShelfNumber())
                .publicationYear(command.getPublicationYear())
                .language(command.getLanguage())
                .pages(command.getPages())
                .status(command.getStatus())
                .condition(command.getCondition())
                .schoolId(existingBook.getSchoolId())
                .libraryId(existingBook.getLibraryId())
                .createdAt(existingBook.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();

        return bookRepository.update(updatedBook);
    }

    @Override
    public void deleteBook(UUID bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new LibraryException("Book not found with id: " + bookId));

        long activeLoans = bookLoanRepository.countByBookId(bookId);
        if (activeLoans > 0) {
            throw new LibraryException("Cannot delete book with active loans");
        }

        boolean deleted = bookRepository.delete(bookId);
        if (!deleted) {
            throw new LibraryException("Failed to delete book");
        }
    }

    @Override
    public PageResult<Book> getBookList(PageInfo pageInfo) {
        return bookRepository.findAll(pageInfo);
    }

    @Override
    public Book getDetailBook(UUID bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new LibraryException("Book not found with id: " + bookId));
    }

    @Override
    public BookLoan borrowBook(BorrowBookCommand command) {
        Book book = bookRepository.findById(command.getBookId())
                .orElseThrow(() -> new LibraryException("Book not found with id: " + command.getBookId()));

        LibraryUser user = libraryUserRepository.findByUserId(command.getBorrowerId())
                .orElseThrow(() -> new LibraryException("Library user not found with id: " + command.getBorrowerId()));

        if (!book.isAvailableForBorrow()) {
            throw new LibraryException("Book is not available for borrowing");
        }

        if (!user.canBorrowBooks()) {
            throw new LibraryException("User cannot borrow more books");
        }

        BookLoan loan = BookLoan.builder()
                .id(UUID.randomUUID())
                .bookId(command.getBookId())
                .borrowerId(command.getBorrowerId())
                .borrowerType(command.getBorrowerType())
                .borrowDate(LocalDateTime.now())
                .dueDate(command.getDueDate())
                .status(BookLoanStatus.BORROWED)
                .notes(command.getNotes())
                .renewalCount(0)
                .maxRenewals(3)
                .fineAmount(0.0)
                .finePaid(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        book.borrowBook();
        user.borrowBook();

        bookRepository.update(book);
        libraryUserRepository.update(user);

        return bookLoanRepository.create(loan);
    }

    @Override
    public BookLoan returnBook(ReturnBookCommand command) {
        BookLoan loan = bookLoanRepository.findById(command.getLoanId())
                .orElseThrow(() -> new LibraryException("Loan not found with id: " + command.getLoanId()));

        if (loan.getStatus() != BookLoanStatus.BORROWED) {
            throw new LibraryException("Loan is not active");
        }

        Book book = bookRepository.findById(loan.getBookId())
                .orElseThrow(() -> new LibraryException("Book not found"));

        LibraryUser user = libraryUserRepository.findByUserId(loan.getBorrowerId())
                .orElseThrow(() -> new LibraryException("Library user not found"));

        book.returnBook();
        user.returnBook();

        loan.returnBook(command.getReturnCondition(), command.getReturnNotes());

        bookRepository.update(book);
        libraryUserRepository.update(user);

        return bookLoanRepository.update(loan);
    }

    @Override
    public BookLoan renewBookLoan(UUID loanId) {
        BookLoan loan = bookLoanRepository.findById(loanId)
                .orElseThrow(() -> new LibraryException("Loan not found with id: " + loanId));

        if (!loan.canRenew()) {
            throw new LibraryException("Cannot renew this loan");
        }

        LocalDateTime newDueDate = loan.getDueDate().plusWeeks(2);
        loan.renewLoan(newDueDate);

        return bookLoanRepository.update(loan);
    }

    @Override
    public LibraryUser registerLibraryUser(RegisterLibraryUserCommand command) {
        if (libraryUserRepository.existsByUserId(command.getUserId())) {
            throw new LibraryException("User already registered in library");
        }

        if (libraryUserRepository.existsByLibraryCardNumber(command.getLibraryCardNumber())) {
            throw new LibraryException("Library card number already exists");
        }

        LibraryUser user = LibraryUser.builder()
                .id(UUID.randomUUID())
                .userId(command.getUserId())
                .userType(command.getUserType())
                .libraryCardNumber(command.getLibraryCardNumber())
                .maxBooksAllowed(command.getMaxBooksAllowed() != null ? command.getMaxBooksAllowed() : 5)
                .currentBooksCount(0)
                .maxRenewalCount(command.getMaxRenewalCount() != null ? command.getMaxRenewalCount() : 3)
                .status(LibraryUserStatus.ACTIVE)
                .registrationDate(LocalDateTime.now())
                .expiryDate(command.getExpiryDate())
                .totalFineAmount(0.0)
                .paidFineAmount(0.0)
                .outstandingFineAmount(0.0)
                .schoolId(command.getSchoolId())
                .libraryId(command.getLibraryId())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return libraryUserRepository.create(user);
    }

    @Override
    public void deactivateLibraryAccount(UUID userId) {
        LibraryUser user = libraryUserRepository.findByUserId(userId)
                .orElseThrow(() -> new LibraryException("Library user not found with id: " + userId));

        if (user.getCurrentBooksCount() > 0) {
            throw new LibraryException("Cannot deactivate account with borrowed books");
        }

        user.setStatus(LibraryUserStatus.INACTIVE);
        user.setUpdatedAt(LocalDateTime.now());

        libraryUserRepository.update(user);
    }

    @Override
    public void sendOverdueNotice(UUID borrowerId) {
        // Implementation would send notification
        // This is a placeholder for the notification logic
    }

    @Override
    public PageResult<BookLoan> trackOverdueBooks(PageInfo pageInfo) {
        return bookLoanRepository.findByStatus("OVERDUE", pageInfo);
    }

    @Override
    public PageResult<BookLoan> viewUserBorrowingHistory(UUID userId, PageInfo pageInfo) {
        return bookLoanRepository.findByBorrowerId(userId, pageInfo);
    }
}
