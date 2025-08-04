package com.coeus.api.models.dtos;

import com.coeus.api.models.enums.LoanStatus;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDate;
import java.util.Objects;

    /*
    /   this DTO goal is to represent the loan data returned by the API responses.
    /   it is used by GET/loans endpoints, and also as the response body for POST /loans.
    /   fields like loanDate, status, and returnDate are managed and populated by the backend.
    */

// defines the name of HATEOAS collection as "loans"
@Relation(collectionRelation = "loans")
public class LoanResponseDTO extends RepresentationModel<LoanResponseDTO> {

    private Long id;
    private String studentName;
    private String bookName;
    private String employeeName;
    private LocalDate loanDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private LoanStatus status;
    private String notes;

    public LoanResponseDTO() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public LocalDate getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(LocalDate loanDate) {
        this.loanDate = loanDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public LoanStatus getStatus() {
        return status;
    }

    public void setStatus(LoanStatus status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        LoanResponseDTO that = (LoanResponseDTO) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(studentName, that.studentName) &&
                Objects.equals(bookName, that.bookName) &&
                Objects.equals(employeeName, that.employeeName) &&
                Objects.equals(loanDate, that.loanDate) &&
                Objects.equals(dueDate, that.dueDate) &&
                Objects.equals(returnDate, that.returnDate) &&
                status == that.status &&
                Objects.equals(notes, that.notes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, studentName, bookName, employeeName, loanDate, dueDate, returnDate, status, notes);
    }
}