package com.coeus.api.models;

import com.coeus.api.models.enums.WarningStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Warning {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_id")
    private Loan loan;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee issuedBy;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WarningStatus status;

    @Column(nullable = false)
    private LocalDateTime issuedAt;

    private LocalDateTime resolvedAt;

    @Column(nullable = false)
    private String reason;

    @Column(columnDefinition = "TEXT")
    private String details;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LocalDateTime getResolvedAt() {
        return resolvedAt;
    }

    public void setResolvedAt(LocalDateTime resolvedAt) {
        this.resolvedAt = resolvedAt;
    }

    public LocalDateTime getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(LocalDateTime issuedAt) {
        this.issuedAt = issuedAt;
    }

    public WarningStatus getStatus() {
        return status;
    }

    public void setStatus(WarningStatus status) {
        this.status = status;
    }

    public Employee getIssuedBy() {
        return issuedBy;
    }

    public void setIssuedBy(Employee issuedBy) {
        this.issuedBy = issuedBy;
    }

    public Loan getLoan() {
        return loan;
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    // sets default values before persisting (status = PENDING, issuedAt = current timestamp).
    @PrePersist
    public void prePersist() {
        this.status = WarningStatus.PENDING;
        this.issuedAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Warning warning = (Warning) o;
        return Objects.equals(id, warning.id) && Objects.equals(student, warning.student) && Objects.equals(loan, warning.loan) && Objects.equals(issuedBy, warning.issuedBy) && Objects.equals(status, warning.status) && Objects.equals(issuedAt, warning.issuedAt) && Objects.equals(resolvedAt, warning.resolvedAt) && Objects.equals(reason, warning.reason) && Objects.equals(details, warning.details);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, student, loan, issuedBy, status, issuedAt, resolvedAt, reason, details);
    }
}
