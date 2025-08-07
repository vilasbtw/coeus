package com.coeus.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateWarningForLoanException extends RuntimeException {

    public DuplicateWarningForLoanException(Long loanId) {
        super("A warning already exists for loan with id " + loanId + ".");
    }

    public DuplicateWarningForLoanException() {
        super("A warning already exists for this loan.");
    }
}
