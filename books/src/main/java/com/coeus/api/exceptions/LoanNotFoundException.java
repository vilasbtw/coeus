package com.coeus.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class LoanNotFoundException extends RuntimeException {

    public LoanNotFoundException(Long loanId) {
        super("Loan with id " + loanId + " was not found.");
    }

    public LoanNotFoundException() {
        super("Loan not found.");
    }
}
