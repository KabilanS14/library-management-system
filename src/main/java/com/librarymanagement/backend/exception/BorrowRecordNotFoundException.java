package com.librarymanagement.backend.exception;

public class BorrowRecordNotFoundException extends RuntimeException {
    public BorrowRecordNotFoundException(String message) {
        super(message);
    }
}
