package com.librarymanagement.backend.service;

import com.librarymanagement.backend.dto.BookRequest;
import com.librarymanagement.backend.dto.BookResponse;
import com.librarymanagement.backend.entity.Book;
import com.librarymanagement.backend.exception.BookNotFoundException;
import com.librarymanagement.backend.exception.DuplicateBookException;
import com.librarymanagement.backend.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @Test
    void createBookShouldSaveBookSuccessfully() {
        BookRequest request = BookRequest.builder()
                .title("Clean Code")
                .author("Robert C. Martin")
                .isbn("9780132350884")
                .category("Programming")
                .publisher("Prentice Hall")
                .publishedYear(2008)
                .language("English")
                .totalCopies(3)
                .build();

        when(bookRepository.existsByIsbn(any())).thenReturn(false);
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> {
            Book book = invocation.getArgument(0);
            book.setId(1L);
            book.setCreatedAt(LocalDateTime.now());
            book.setUpdatedAt(LocalDateTime.now());
            return book;
        });

        BookResponse response = bookService.createBook(request);

        assertNotNull(response);
        assertEquals("Clean Code", response.getTitle());
    }

    @Test
    void createBookShouldThrowDuplicateBookException() {
        BookRequest request = BookRequest.builder()
                .isbn("123")
                .build();

        when(bookRepository.existsByIsbn("123")).thenReturn(true);

        assertThrows(DuplicateBookException.class, () -> bookService.createBook(request));
    }

    @Test
    void getBookByIdShouldThrowWhenNotFound() {
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookService.getBookById(99L));
    }
}
