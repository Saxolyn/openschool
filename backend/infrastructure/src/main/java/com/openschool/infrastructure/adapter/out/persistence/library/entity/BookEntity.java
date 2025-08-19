package com.openschool.infrastructure.adapter.out.persistence.library.entity;

import com.openschool.domain.library.Book;
import com.openschool.domain.library.BookCondition;
import com.openschool.domain.library.BookStatus;
import com.openschool.infrastructure.adapter.out.persistence.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "books")
public class BookEntity extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(unique = true)
    private String isbn;
    
    private String author;
    private String publisher;
    private String category;
    private String description;
    
    private Integer totalCopies;
    private Integer availableCopies;
    private Integer borrowedCopies;
    private String location;
    private String shelfNumber;
    
    private Integer publicationYear;
    private String language;
    private Integer pages;
    
    @Enumerated(EnumType.STRING)
    private BookStatus status;
    
    @Enumerated(EnumType.STRING)
    private BookCondition condition;
    
    private UUID schoolId;
    private UUID libraryId;
    
    public static BookEntity fromDomain(Book book) {
        if (book == null) return null;
        
        return BookEntity.builder()
                .id(book.getId())
                .title(book.getTitle())
                .isbn(book.getIsbn())
                .author(book.getAuthor())
                .publisher(book.getPublisher())
                .category(book.getCategory())
                .description(book.getDescription())
                .totalCopies(book.getTotalCopies())
                .availableCopies(book.getAvailableCopies())
                .borrowedCopies(book.getBorrowedCopies())
                .location(book.getLocation())
                .shelfNumber(book.getShelfNumber())
                .publicationYear(book.getPublicationYear())
                .language(book.getLanguage())
                .pages(book.getPages())
                .status(book.getStatus())
                .condition(book.getCondition())
                .schoolId(book.getSchoolId())
                .libraryId(book.getLibraryId())
                .build();
    }
    
    public Book toDomain() {
        return Book.builder()
                .id(this.id)
                .title(this.title)
                .isbn(this.isbn)
                .author(this.author)
                .publisher(this.publisher)
                .category(this.category)
                .description(this.description)
                .totalCopies(this.totalCopies)
                .availableCopies(this.availableCopies)
                .borrowedCopies(this.borrowedCopies)
                .location(this.location)
                .shelfNumber(this.shelfNumber)
                .publicationYear(this.publicationYear)
                .language(this.language)
                .pages(this.pages)
                .status(this.status)
                .condition(this.condition)
                .schoolId(this.schoolId)
                .libraryId(this.libraryId)
                .createdAt(this.getCreatedAt())
                .updatedAt(this.getUpdatedAt())
                .build();
    }
}
