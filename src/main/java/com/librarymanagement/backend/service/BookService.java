package com.librarymanagement.backend.service;

import com.librarymanagement.backend.dto.BookRequest;
import com.librarymanagement.backend.dto.BookResponse;
import com.librarymanagement.backend.entity.Book;
import com.librarymanagement.backend.exception.BookDeletionException;
import com.librarymanagement.backend.exception.BookNotFoundException;
import com.librarymanagement.backend.exception.DuplicateBookException;
import com.librarymanagement.backend.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookService {

    private final BookRepository bookRepository;

    @Transactional
    public BookResponse createBook(BookRequest request) {
        if (bookRepository.existsByIsbn(request.getIsbn())) {
            throw new DuplicateBookException("Book with ISBN " + request.getIsbn() + " already exists");
        }

        Book book = Book.builder()
                .title(request.getTitle())
                .author(request.getAuthor())
                .isbn(request.getIsbn())
                .category(request.getCategory())
                .publisher(request.getPublisher())
                .publishedYear(request.getPublishedYear())
                .language(request.getLanguage())
                .totalCopies(request.getTotalCopies())
                .availableCopies(request.getTotalCopies())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Book savedBook = bookRepository.save(book);
        log.info("Book created with ISBN: {}", savedBook.getIsbn());
        return BookResponse.from(savedBook);
    }

    @Transactional(readOnly = true)
    public Page<BookResponse> getAllBooks(int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return bookRepository.findAll(pageable).map(BookResponse::from);
    }

    @Transactional(readOnly = true)
    public BookResponse getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found with id: " + id));
        return BookResponse.from(book);
    }

    @Transactional
    public BookResponse updateBook(Long id, BookRequest request) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found with id: " + id));

        if (request.getTitle() != null) book.setTitle(request.getTitle());
        if (request.getAuthor() != null) book.setAuthor(request.getAuthor());
        if (request.getCategory() != null) book.setCategory(request.getCategory());
        if (request.getPublisher() != null) book.setPublisher(request.getPublisher());
        if (request.getLanguage() != null) book.setLanguage(request.getLanguage());
        if (request.getPublishedYear() != null) book.setPublishedYear(request.getPublishedYear());

        if (request.getTotalCopies() != null) {
            int newTotalCopies = request.getTotalCopies();
            if (newTotalCopies < book.getAvailableCopies()) {
                throw new IllegalArgumentException("Total copies cannot be less than currently available copies");
            }
            book.setTotalCopies(newTotalCopies);
            book.setAvailableCopies(book.getAvailableCopies());
        }

        book.setUpdatedAt(LocalDateTime.now());
        Book updatedBook = bookRepository.save(book);
        log.info("Book updated with id: {}", updatedBook.getId());
        return BookResponse.from(updatedBook);
    }

    @Transactional
    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found with id: " + id));

        if (book.getAvailableCopies() < book.getTotalCopies()) {
            throw new BookDeletionException("Cannot delete book because it is currently borrowed");
        }

        bookRepository.delete(book);
        log.info("Book deleted with id: {}", id);
    }

    @Transactional(readOnly = true)
    public List<BookResponse> searchBooks(String keyword) {
        return bookRepository.searchBooks(keyword).stream()
                .map(BookResponse::from)
                .toList();
    }
}
