package com.coeus.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class StudentNotEnabledException extends RuntimeException {

    public StudentNotEnabledException(String message) {
        super(message);
    }

    public StudentNotEnabledException() {
      super("Student is not enabled to borrow books");
    }
}
