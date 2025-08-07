package com.coeus.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class UnauthorizedWarningDeleteException extends RuntimeException {

    public UnauthorizedWarningDeleteException() {
        super("You do not have permission to delete this warning.");
    }
}
