package com.librarymanagement.backend.dto;

import com.librarymanagement.backend.entity.Book;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookResponse {
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private String category;
    private String publisher;
    private Integer publishedYear;
    private String language;
    private Integer totalCopies;
    private Integer availableCopies;

    public static BookResponse from(Book book) {
        return BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .isbn(book.getIsbn())
                .category(book.getCategory())
                .publisher(book.getPublisher())
                .publishedYear(book.getPublishedYear())
                .language(book.getLanguage())
                .totalCopies(book.getTotalCopies())
                .availableCopies(book.getAvailableCopies())
                .build();
    }
}
