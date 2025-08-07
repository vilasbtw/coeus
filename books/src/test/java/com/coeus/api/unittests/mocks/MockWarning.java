package com.coeus.api.unittests.mocks;

import com.coeus.api.models.Warning;
import com.coeus.api.models.dtos.WarningDTO;
import com.coeus.api.models.enums.WarningStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MockWarning {

    private final MockStudent mockStudent = new MockStudent();
    private final MockEmployee mockEmployee = new MockEmployee();
    private final MockLoan mockLoan = new MockLoan();

    public Warning mockWarningEntity() {
        return mockWarningEntity(1);
    }

    public Warning mockWarningEntity(Integer number) {
        Warning warning = new Warning();
        warning.setId(number.longValue());
        warning.setStudent(mockStudent.mockStudentEntity(number));
        warning.setLoan(mockLoan.mockLoanEntity(number));
        warning.setIssuedBy(mockEmployee.mockEmployeeEntity(number));
        warning.setReason("Reason: " + number);
        warning.setDetails("Details for warning " + number);
        warning.setStatus(WarningStatus.PENDING);
        warning.setIssuedAt(LocalDateTime.of(2025, 8, 7, 10, 0));
        warning.setResolvedAt(null);
        return warning;
    }

    public List<Warning> mockWarningEntities() {
        List<Warning> warnings = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            warnings.add(mockWarningEntity(i));
        }
        return warnings;
    }

    public WarningDTO mockWarningDTO() {
        return mockWarningDTO(1);
    }

    public WarningDTO mockWarningDTO(Integer number) {
        WarningDTO dto = new WarningDTO();
        dto.setId(number.longValue());
        dto.setStudentId(number.longValue());
        dto.setLoanId(number.longValue());
        dto.setEmployeeId(number.longValue());
        dto.setReason("Reason: " + number);
        dto.setDetails("Details for warning " + number);
        dto.setStatus(WarningStatus.PENDING);
        dto.setIssuedAt(LocalDateTime.of(2025, 8, 7, 10, 0));
        dto.setResolvedAt(null);
        return dto;
    }

    public List<WarningDTO> mockWarningDTOs() {
        List<WarningDTO> dtos = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            dtos.add(mockWarningDTO(i));
        }
        return dtos;
    }
}