package com.coeus.api.repositories;

import com.coeus.api.models.Loan;
import com.coeus.api.models.enums.LoanStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {

    // list all loans by Student
    List<Loan> findByStudentId(Long studentId);

    // list all loans by their status, e.g.: ON_GOING, LATE or RETURNED.
    List<Loan> findByStatus(LoanStatus status);
}