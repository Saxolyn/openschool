package com.openschool.administration.library.port.out;

import com.openschool.common.pageable.PageInfo;
import com.openschool.common.pageable.PageResult;
import com.openschool.domain.library.Book;

import java.util.Optional;
import java.util.UUID;

public interface BookRepositoryPort {
    
    Book create(Book book);
    
    Book update(Book book);
    
    Optional<Book> findById(UUID bookId);
    
    Optional<Book> findByIsbn(String isbn);
    
    PageResult<Book> findAll(PageInfo pageInfo);
    
    PageResult<Book> findBySchoolId(UUID schoolId, PageInfo pageInfo);
    
    PageResult<Book> findByCategory(String category, PageInfo pageInfo);
    
    PageResult<Book> findByAuthor(String author, PageInfo pageInfo);
    
    PageResult<Book> search(String searchTerm, PageInfo pageInfo);
    
    boolean delete(UUID bookId);
    
    boolean existsByIsbn(String isbn);
    
    long count();
    
    long countBySchoolId(UUID schoolId);
    
    long countAvailableBooks();
}
