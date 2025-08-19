package com.openschool.administration.library.port.out;

import com.openschool.common.pageable.PageInfo;
import com.openschool.common.pageable.PageResult;
import com.openschool.domain.library.BookLoan;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookLoanRepositoryPort {
    
    BookLoan create(BookLoan loan);
    
    BookLoan update(BookLoan loan);
    
    Optional<BookLoan> findById(UUID loanId);
    
    PageResult<BookLoan> findAll(PageInfo pageInfo);
    
    PageResult<BookLoan> findByBookId(UUID bookId, PageInfo pageInfo);
    
    PageResult<BookLoan> findByBorrowerId(UUID borrowerId, PageInfo pageInfo);
    
    PageResult<BookLoan> findByStatus(String status, PageInfo pageInfo);
    
    List<BookLoan> findOverdueLoans();
    
    List<BookLoan> findActiveLoans();
    
    boolean delete(UUID loanId);
    
    long count();
    
    long countByBookId(UUID bookId);
    
    long countByBorrowerId(UUID borrowerId);
    
    long countOverdueLoans();
}
