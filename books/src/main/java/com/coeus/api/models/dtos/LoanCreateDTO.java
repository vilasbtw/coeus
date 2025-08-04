package com.coeus.api.models.dtos;

import java.time.LocalDate;
import java.util.Objects;

    /*
    /   this DTO goal is to represent only the essential data to create a new loan in the system.
    /   fields like: loanDate, status, returnDate, employee, etc. are managed by the backend.
    /   used only by POST/loans requests, where the Employee will
    /   link the Book to a Student, defining a due date for the return.
    */

// since this object won't be returned by the API,
// @Relational and RepresentationalModel are not necessary.
public class LoanCreateDTO {

    private Long studentId;
    private Long bookId;
    private LocalDate dueDate;

    public LoanCreateDTO() {
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        LoanCreateDTO that = (LoanCreateDTO) o;
        return Objects.equals(studentId, that.studentId) && Objects.equals(bookId, that.bookId) && Objects.equals(dueDate, that.dueDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), studentId, bookId, dueDate);
    }
}
