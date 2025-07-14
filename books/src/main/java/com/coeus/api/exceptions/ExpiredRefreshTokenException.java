package com.coeus.api.exceptions;

public class ExpiredRefreshTokenException extends RuntimeException {
    public ExpiredRefreshTokenException(String message) {
        super(message);
    }

    public ExpiredRefreshTokenException() {
        super("Refresh Token is expired.");
    }
}
