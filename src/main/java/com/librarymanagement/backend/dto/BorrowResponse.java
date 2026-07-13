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
public class BorrowResponse {
    private Long id;
    private String bookName;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private BorrowStatus status;

    public static BorrowResponse from(BorrowRecord record) {
        return BorrowResponse.builder()
                .id(record.getId())
                .bookName(record.getBook().getTitle())
                .borrowDate(record.getBorrowDate())
                .dueDate(record.getDueDate())
                .status(record.getStatus())
                .build();
    }
}
