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
public class ReturnResponse {
    private Long id;
    private String bookName;
    private LocalDate returnDate;
    private double fine;
    private BorrowStatus status;

    public static ReturnResponse from(BorrowRecord record) {
        return ReturnResponse.builder()
                .id(record.getId())
                .bookName(record.getBook().getTitle())
                .returnDate(record.getReturnDate())
                .fine(record.getFine())
                .status(record.getStatus())
                .build();
    }
}
