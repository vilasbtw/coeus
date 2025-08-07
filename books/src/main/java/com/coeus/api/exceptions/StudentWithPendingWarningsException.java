package com.coeus.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class StudentWithPendingWarningsException extends RuntimeException {

    public StudentWithPendingWarningsException(Long studentId) {
        super("Student with id " + studentId + " has pending warnings and cannot borrow new books.");
    }

    public StudentWithPendingWarningsException() {
        super("Student has pending warnings and cannot borrow new books.");
    }
}
