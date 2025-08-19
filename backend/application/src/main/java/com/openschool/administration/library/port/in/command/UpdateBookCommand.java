package com.openschool.administration.library.port.in.command;

import com.openschool.domain.library.BookCondition;
import com.openschool.domain.library.BookStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateBookCommand {
    private UUID id;
    private String title;
    private String isbn;
    private String author;
    private String publisher;
    private String category;
    private String description;
    private String location;
    private String shelfNumber;
    private Integer publicationYear;
    private String language;
    private Integer pages;
    private BookStatus status;
    private BookCondition condition;
}
