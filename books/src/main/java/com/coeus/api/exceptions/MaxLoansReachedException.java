package com.coeus.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class MaxLoansReachedException extends RuntimeException {
    public MaxLoansReachedException(String message) {
        super(message);
    }

    public MaxLoansReachedException() {
        super("Student has reached the maximum number of active loans");
    }
}
