package com.coeus.api.repositories;

import com.coeus.api.models.Warning;
import com.coeus.api.models.enums.WarningStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WarningRepository extends JpaRepository<Warning, Long> {

    List<Warning> findByStudentId(Long studentId);

    boolean existsByLoanId(Long loanId);

    boolean existsByStudentIdAndStatus(Long studentId, WarningStatus status);
}