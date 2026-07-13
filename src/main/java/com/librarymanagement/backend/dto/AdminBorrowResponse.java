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
public class AdminBorrowResponse {
    private Long id;
    private String memberName;
    private String bookName;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private double fine;
    private BorrowStatus status;

    public static AdminBorrowResponse from(BorrowRecord record) {
        return AdminBorrowResponse.builder()
                .id(record.getId())
                .memberName(record.getUser().getName())
                .bookName(record.getBook().getTitle())
                .borrowDate(record.getBorrowDate())
                .dueDate(record.getDueDate())
                .returnDate(record.getReturnDate())
                .fine(record.getFine())
                .status(record.getStatus())
                .build();
    }
}
