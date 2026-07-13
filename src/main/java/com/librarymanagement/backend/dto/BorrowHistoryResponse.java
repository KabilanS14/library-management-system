package com.librarymanagement.backend.dto;

import com.librarymanagement.backend.entity.BorrowRecord;
import com.librarymanagement.backend.enums.BorrowStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BorrowHistoryResponse {
    private Long id;
    private String bookName;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private double fine;
    private BorrowStatus status;

    public static BorrowHistoryResponse from(BorrowRecord record) {
        return BorrowHistoryResponse.builder()
                .id(record.getId())
                .bookName(record.getBook().getTitle())
                .borrowDate(record.getBorrowDate())
                .dueDate(record.getDueDate())
                .returnDate(record.getReturnDate())
                .fine(record.getFine())
                .status(record.getStatus())
                .build();
    }
}
