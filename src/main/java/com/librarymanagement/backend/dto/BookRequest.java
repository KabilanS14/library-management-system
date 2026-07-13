package com.librarymanagement.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookRequest {

    @NotBlank(message = "Title is required")
    @Size(max = 200, message = "Title must not exceed 200 characters")
    private String title;

    @NotBlank(message = "Author is required")
    @Size(max = 200, message = "Author must not exceed 200 characters")
    private String author;

    @NotBlank(message = "ISBN is required")
    @Pattern(regexp = "^[0-9Xx\\-]{10,17}$", message = "ISBN format is invalid")
    private String isbn;

    @NotBlank(message = "Category is required")
    private String category;

    @NotBlank(message = "Publisher is required")
    private String publisher;

    @Positive(message = "Published year must be positive")
    private Integer publishedYear;

    @NotBlank(message = "Language is required")
    private String language;

    @Positive(message = "Total copies must be greater than zero")
    private Integer totalCopies;
}
