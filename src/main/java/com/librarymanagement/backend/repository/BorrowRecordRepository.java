package com.librarymanagement.backend.repository;

import com.librarymanagement.backend.entity.Book;
import com.librarymanagement.backend.entity.BorrowRecord;
import com.librarymanagement.backend.entity.User;
import com.librarymanagement.backend.enums.BorrowStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {
    List<BorrowRecord> findByUserOrderByBorrowDateDesc(User user);

    List<BorrowRecord> findByUserAndStatusOrderByBorrowDateDesc(User user, BorrowStatus status);

    boolean existsByUserAndBookAndStatus(User user, Book book, BorrowStatus status);

    List<BorrowRecord> findByStatusAndDueDateBefore(BorrowStatus status, LocalDate date);

    List<BorrowRecord> findByFineGreaterThan(double fine);
}
