package com.librarymanagement.backend.controller;

import com.librarymanagement.backend.dto.BookRequest;
import com.librarymanagement.backend.dto.BookResponse;
import com.librarymanagement.backend.service.BookService;
import com.librarymanagement.backend.util.ApiResponseWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Books", description = "Book management APIs")
public class BookController {

    private final BookService bookService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Book created successfully"),
            @ApiResponse(responseCode = "409", description = "Book already exists")
    })
    public ResponseEntity<ApiResponseWrapper<BookResponse>> createBook(
            @Valid @RequestBody BookRequest request) {

        BookResponse data = bookService.createBook(request);

        log.info("Book created: {}", request.getTitle());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseWrapper.<BookResponse>builder()
                        .success(true)
                        .message("Book created successfully")
                        .data(data)
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','MEMBER')")
    @Operation(summary = "Get all books with pagination and sorting")
    public ResponseEntity<ApiResponseWrapper<Page<BookResponse>>> getAllBooks(
            @Parameter(description = "Page number")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Page size")
            @RequestParam(defaultValue = "10") int size,

            @Parameter(description = "Sort field")
            @RequestParam(defaultValue = "title") String sort) {

        Page<BookResponse> pageResult = bookService.getAllBooks(page, size, sort);

        return ResponseEntity.ok(
                ApiResponseWrapper.<Page<BookResponse>>builder()
                        .success(true)
                        .message("Books retrieved successfully")
                        .data(pageResult)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MEMBER')")
    @Operation(summary = "Get a book by ID")
    public ResponseEntity<ApiResponseWrapper<BookResponse>> getBookById(
            @PathVariable Long id) {

        BookResponse data = bookService.getBookById(id);

        return ResponseEntity.ok(
                ApiResponseWrapper.<BookResponse>builder()
                        .success(true)
                        .message("Book retrieved successfully")
                        .data(data)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update an existing book")
    public ResponseEntity<ApiResponseWrapper<BookResponse>> updateBook(
            @PathVariable Long id,
            @Valid @RequestBody BookRequest request) {

        BookResponse data = bookService.updateBook(id, request);

        log.info("Book updated with id: {}", id);

        return ResponseEntity.ok(
                ApiResponseWrapper.<BookResponse>builder()
                        .success(true)
                        .message("Book updated successfully")
                        .data(data)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a book")
    public ResponseEntity<ApiResponseWrapper<Void>> deleteBook(
            @PathVariable Long id) {

        bookService.deleteBook(id);

        log.info("Book deleted with id: {}", id);

        return ResponseEntity.ok(
                ApiResponseWrapper.<Void>builder()
                        .success(true)
                        .message("Book deleted successfully")
                        .data(null)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN','MEMBER')")
    @Operation(summary = "Search books by keyword")
    public ResponseEntity<ApiResponseWrapper<List<BookResponse>>> searchBooks(
            @RequestParam String keyword) {

        List<BookResponse> data = bookService.searchBooks(keyword);

        return ResponseEntity.ok(
                ApiResponseWrapper.<List<BookResponse>>builder()
                        .success(true)
                        .message("Books found successfully")
                        .data(data)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }
}