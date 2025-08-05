package com.coeus.api.exceptions;

public class LoanAlreadyReturnedException extends RuntimeException {
    public LoanAlreadyReturnedException(String message) {
        super(message);
    }

    public LoanAlreadyReturnedException() {
        super("Loan has already been returned");
    }
}
