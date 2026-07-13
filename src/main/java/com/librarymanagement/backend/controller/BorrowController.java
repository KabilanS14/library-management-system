package com.librarymanagement.backend.controller;

import com.librarymanagement.backend.dto.AdminBorrowResponse;
import com.librarymanagement.backend.dto.BorrowHistoryResponse;
import com.librarymanagement.backend.dto.BorrowResponse;
import com.librarymanagement.backend.dto.ReturnResponse;
import com.librarymanagement.backend.service.BorrowService;
import com.librarymanagement.backend.util.ApiResponseWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Borrowing", description = "Borrow, return, and history APIs")
public class BorrowController {

    private final BorrowService borrowService;

    @PostMapping("/borrow/{bookId}")
    @PreAuthorize("hasRole('MEMBER')")
    @Operation(summary = "Borrow a book")
    public ResponseEntity<ApiResponseWrapper<BorrowResponse>> borrowBook(@PathVariable Long bookId) {
        BorrowResponse data = borrowService.borrowBook(bookId);
        log.info("Borrow request completed for book id: {}", bookId);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponseWrapper.<BorrowResponse>builder()
                .success(true)
                .message("Book borrowed successfully")
                .data(data)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @PostMapping("/return/{borrowId}")
    @PreAuthorize("hasRole('MEMBER')")
    @Operation(summary = "Return a borrowed book")
    public ResponseEntity<ApiResponseWrapper<ReturnResponse>> returnBook(@PathVariable Long borrowId) {
        ReturnResponse data = borrowService.returnBook(borrowId);
        log.info("Book returned for borrow id: {}", borrowId);
        return ResponseEntity.ok(ApiResponseWrapper.<ReturnResponse>builder()
                .success(true)
                .message("Book returned successfully")
                .data(data)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @GetMapping("/borrow/history")
    @PreAuthorize("hasRole('MEMBER')")
    @Operation(summary = "Get authenticated member borrow history")
    public ResponseEntity<ApiResponseWrapper<List<BorrowHistoryResponse>>> getBorrowHistory() {
        List<BorrowHistoryResponse> data = borrowService.getBorrowHistory();
        return ResponseEntity.ok(ApiResponseWrapper.<List<BorrowHistoryResponse>>builder()
                .success(true)
                .message("Borrow history retrieved successfully")
                .data(data)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @GetMapping("/borrow/current")
    @PreAuthorize("hasRole('MEMBER')")
    @Operation(summary = "Get currently borrowed books for the authenticated member")
    public ResponseEntity<ApiResponseWrapper<List<BorrowHistoryResponse>>> getCurrentBorrowedBooks() {
        List<BorrowHistoryResponse> data = borrowService.getCurrentBorrowedBooks();
        return ResponseEntity.ok(ApiResponseWrapper.<List<BorrowHistoryResponse>>builder()
                .success(true)
                .message("Current borrowed books retrieved successfully")
                .data(data)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @GetMapping("/admin/borrow-records")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all borrow records for admin")
    public ResponseEntity<ApiResponseWrapper<Page<AdminBorrowResponse>>> getAllBorrowRecords(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort) {
        Page<AdminBorrowResponse> data = borrowService.getAllBorrowRecords(page, size, sort);
        return ResponseEntity.ok(ApiResponseWrapper.<Page<AdminBorrowResponse>>builder()
                .success(true)
                .message("Borrow records retrieved successfully")
                .data(data)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @GetMapping("/admin/borrow-records/overdue")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get overdue borrow records")
    public ResponseEntity<ApiResponseWrapper<List<AdminBorrowResponse>>> getOverdueBooks() {
        List<AdminBorrowResponse> data = borrowService.getOverdueBooks();
        return ResponseEntity.ok(ApiResponseWrapper.<List<AdminBorrowResponse>>builder()
                .success(true)
                .message("Overdue books retrieved successfully")
                .data(data)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @GetMapping("/admin/borrow-records/fines")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get borrow records with pending fines")
    public ResponseEntity<ApiResponseWrapper<List<AdminBorrowResponse>>> getPendingFineRecords() {
        List<AdminBorrowResponse> data = borrowService.getPendingFineRecords();
        return ResponseEntity.ok(ApiResponseWrapper.<List<AdminBorrowResponse>>builder()
                .success(true)
                .message("Pending fine records retrieved successfully")
                .data(data)
                .timestamp(LocalDateTime.now())
                .build());
    }
}
