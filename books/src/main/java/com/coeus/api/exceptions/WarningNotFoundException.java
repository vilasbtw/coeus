package com.coeus.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class WarningNotFoundException extends RuntimeException {

    public WarningNotFoundException(Long id) {
        super("Warning with id " + id + " was not found.");
    }

    public WarningNotFoundException() {
        super("Warning not found.");
    }
}
