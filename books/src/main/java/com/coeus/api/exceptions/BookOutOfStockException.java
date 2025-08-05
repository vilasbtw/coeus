package com.coeus.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class BookOutOfStockException extends RuntimeException {
    public BookOutOfStockException(String message) {
        super(message);
    }

    public BookOutOfStockException() {
      super("Book is out of stock");
    }
}
