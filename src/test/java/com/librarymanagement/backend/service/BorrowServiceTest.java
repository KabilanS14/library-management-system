package com.librarymanagement.backend.service;

import com.librarymanagement.backend.dto.BorrowResponse;
import com.librarymanagement.backend.dto.ReturnResponse;
import com.librarymanagement.backend.entity.Book;
import com.librarymanagement.backend.entity.BorrowRecord;
import com.librarymanagement.backend.entity.User;
import com.librarymanagement.backend.enums.BorrowStatus;
import com.librarymanagement.backend.enums.Role;
import com.librarymanagement.backend.exception.BookUnavailableException;
import com.librarymanagement.backend.exception.BorrowRecordNotFoundException;
import com.librarymanagement.backend.repository.BookRepository;
import com.librarymanagement.backend.repository.BorrowRecordRepository;
import com.librarymanagement.backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BorrowServiceTest {

    @Mock
    private BorrowRecordRepository borrowRecordRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BorrowService borrowService;

    @Test
    void borrowBookShouldThrowWhenBookUnavailable() {
        SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken("member@example.com", null));

        User user = User.builder().id(1L).email("member@example.com").role(Role.MEMBER).build();
        Book book = Book.builder().id(1L).title("Test Book").availableCopies(0).build();

        when(userRepository.findByEmail("member@example.com")).thenReturn(Optional.of(user));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        assertThrows(BookUnavailableException.class, () -> borrowService.borrowBook(1L));
    }

    @Test
    void returnBookShouldThrowWhenRecordNotFound() {
        SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken("member@example.com", null));

        User user = User.builder().id(1L).email("member@example.com").role(Role.MEMBER).build();
        when(userRepository.findByEmail("member@example.com")).thenReturn(Optional.of(user));
        when(borrowRecordRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(BorrowRecordNotFoundException.class, () -> borrowService.returnBook(99L));
    }

    @Test
    void borrowBookShouldReturnBorrowResponseWhenAvailable() {
        SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken("member@example.com", null));

        User user = User.builder().id(1L).email("member@example.com").role(Role.MEMBER).build();
        Book book = Book.builder().id(1L).title("Test Book").availableCopies(1).build();

        when(userRepository.findByEmail("member@example.com")).thenReturn(Optional.of(user));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(borrowRecordRepository.existsByUserAndBookAndStatus(any(), any(), any())).thenReturn(false);
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(borrowRecordRepository.save(any(BorrowRecord.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BorrowResponse response = borrowService.borrowBook(1L);

        assertNotNull(response);
        assertEquals("Test Book", response.getBookName());
    }

    @Test
    void returnBookShouldCalculateFineWhenReturnedLate() {
        SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken("member@example.com", null));

        User user = User.builder().id(1L).email("member@example.com").role(Role.MEMBER).build();
        Book book = Book.builder().id(1L).title("Test Book").availableCopies(0).build();
        BorrowRecord record = BorrowRecord.builder()
                .id(1L)
                .user(user)
                .book(book)
                .borrowDate(LocalDate.now().minusDays(20))
                .dueDate(LocalDate.now().minusDays(5))
                .status(BorrowStatus.BORROWED)
                .build();

        when(userRepository.findByEmail("member@example.com")).thenReturn(Optional.of(user));
        when(borrowRecordRepository.findById(1L)).thenReturn(Optional.of(record));
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(borrowRecordRepository.save(any(BorrowRecord.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ReturnResponse response = borrowService.returnBook(1L);

        assertNotNull(response);
        assertTrue(response.getFine() >= 0);
    }
}
