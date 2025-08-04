package com.coeus.api.models.enums;

public enum LoanStatus {

    ON_GOING,  // loan is active and within the due date period.
    RETURNED,  // the book has been returned. the loan is closed.
    LATE       // the due date has passed and the book has not been returned yet.
}