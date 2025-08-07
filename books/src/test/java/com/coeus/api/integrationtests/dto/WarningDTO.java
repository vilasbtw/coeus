package com.coeus.api.integrationtests.dto;

import com.coeus.api.models.enums.WarningStatus;

import java.time.LocalDateTime;
import java.util.Objects;


public class WarningDTO {

    private Long id;
    private Long studentId;
    private Long loanId;
    private Long employeeId;
    private String reason;
    private String details;
    private WarningStatus status;
    private LocalDateTime issuedAt;
    private LocalDateTime resolvedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public Long getLoanId() { return loanId; }
    public void setLoanId(Long loanId) { this.loanId = loanId; }

    public Long getEmployeeId() { return employeeId; }
    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }

    public WarningStatus getStatus() { return status; }
    public void setStatus(WarningStatus status) { this.status = status; }

    public LocalDateTime getIssuedAt() { return issuedAt; }
    public void setIssuedAt(LocalDateTime issuedAt) { this.issuedAt = issuedAt; }

    public LocalDateTime getResolvedAt() { return resolvedAt; }
    public void setResolvedAt(LocalDateTime resolvedAt) { this.resolvedAt = resolvedAt; }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        WarningDTO that = (WarningDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(studentId, that.studentId) && Objects.equals(loanId, that.loanId) && Objects.equals(employeeId, that.employeeId) && Objects.equals(reason, that.reason) && Objects.equals(details, that.details) && status == that.status && Objects.equals(issuedAt, that.issuedAt) && Objects.equals(resolvedAt, that.resolvedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, studentId, loanId, employeeId, reason, details, status, issuedAt, resolvedAt);
    }
}
