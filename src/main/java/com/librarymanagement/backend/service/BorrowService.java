package com.librarymanagement.backend.service;

import com.librarymanagement.backend.dto.AdminBorrowResponse;
import com.librarymanagement.backend.dto.BorrowHistoryResponse;
import com.librarymanagement.backend.dto.BorrowResponse;
import com.librarymanagement.backend.dto.ReturnResponse;
import com.librarymanagement.backend.entity.Book;
import com.librarymanagement.backend.entity.BorrowRecord;
import com.librarymanagement.backend.entity.User;
import com.librarymanagement.backend.enums.BorrowStatus;
import com.librarymanagement.backend.exception.*;
import com.librarymanagement.backend.repository.BookRepository;
import com.librarymanagement.backend.repository.BorrowRecordRepository;
import com.librarymanagement.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BorrowService {

    private final BorrowRecordRepository borrowRecordRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @Transactional
    public BorrowResponse borrowBook(Long bookId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found"));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book not found with id: " + bookId));

        if (book.getAvailableCopies() <= 0) {
            throw new BookUnavailableException("Book is currently unavailable");
        }

        boolean alreadyBorrowed = borrowRecordRepository.existsByUserAndBookAndStatus(user, book, BorrowStatus.BORROWED);
        if (alreadyBorrowed) {
            throw new AlreadyBorrowedException("You have already borrowed this book and have not returned it yet");
        }

        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);

        BorrowRecord record = BorrowRecord.builder()
                .user(user)
                .book(book)
                .borrowDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(14))
                .fine(0)
                .status(BorrowStatus.BORROWED)
                .build();

        BorrowRecord savedRecord = borrowRecordRepository.save(record);
        log.info("Book borrowed: {} by {}", book.getTitle(), user.getEmail());
        return BorrowResponse.from(savedRecord);
    }

    @Transactional
    public ReturnResponse returnBook(Long borrowId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found"));

        BorrowRecord record = borrowRecordRepository.findById(borrowId)
                .orElseThrow(() -> new BorrowRecordNotFoundException("Borrow record not found with id: " + borrowId));

        if (!record.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedOperationException("You cannot return another user's borrowed book");
        }

        if (record.getStatus() == BorrowStatus.RETURNED) {
            throw new BookAlreadyReturnedException("This book has already been returned");
        }

        LocalDate returnDate = LocalDate.now();
        double fine = 0;
        if (returnDate.isAfter(record.getDueDate())) {
            long lateDays = ChronoUnit.DAYS.between(record.getDueDate(), returnDate);
            fine = lateDays * 10;
        }

        record.setReturnDate(returnDate);
        record.setFine(fine);
        record.setStatus(BorrowStatus.RETURNED);

        Book book = record.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepository.save(book);

        BorrowRecord savedRecord = borrowRecordRepository.save(record);
        log.info("Book returned: {} with fine {}", book.getTitle(), fine);
        return ReturnResponse.from(savedRecord);
    }

    @Transactional(readOnly = true)
    public List<BorrowHistoryResponse> getBorrowHistory() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found"));
        return borrowRecordRepository.findByUserOrderByBorrowDateDesc(user).stream()
                .map(BorrowHistoryResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<BorrowHistoryResponse> getCurrentBorrowedBooks() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found"));
        return borrowRecordRepository.findByUserAndStatusOrderByBorrowDateDesc(user, BorrowStatus.BORROWED).stream()
                .map(BorrowHistoryResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public Page<AdminBorrowResponse> getAllBorrowRecords(int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return borrowRecordRepository.findAll(pageable).map(AdminBorrowResponse::from);
    }

    @Transactional(readOnly = true)
    public List<AdminBorrowResponse> getOverdueBooks() {
        return borrowRecordRepository.findByStatusAndDueDateBefore(BorrowStatus.BORROWED, LocalDate.now()).stream()
                .map(AdminBorrowResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AdminBorrowResponse> getPendingFineRecords() {
        return borrowRecordRepository.findByFineGreaterThan(0).stream()
                .map(AdminBorrowResponse::from)
                .toList();
    }
}
