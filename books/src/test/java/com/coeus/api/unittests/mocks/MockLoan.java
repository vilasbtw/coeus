package com.coeus.api.unittests.mocks;

import com.coeus.api.models.Loan;
import com.coeus.api.models.dtos.LoanCreateDTO;
import com.coeus.api.models.dtos.LoanResponseDTO;
import com.coeus.api.models.enums.LoanStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MockLoan {

    private final MockBook mockBook = new MockBook();
    private final MockStudent mockStudent = new MockStudent();
    private final MockEmployee mockEmployee = new MockEmployee();

    public Loan mockLoanEntity() {
        return mockLoanEntity(1);
    }

    public Loan mockLoanEntity(Integer number) {
        Loan loan = new Loan();
        loan.setId(number.longValue());
        loan.setStudent(mockStudent.mockStudentEntity(number));
        loan.setBook(mockBook.mockBookEntity(number));
        loan.setEmployee(mockEmployee.mockEmployeeEntity(number));
        loan.setLoanDate(LocalDate.of(2025, 8, 5));
        loan.setDueDate(LocalDate.of(2025, 8, 19));
        loan.setReturnDate(null);
        loan.setStatus(LoanStatus.ON_GOING);
        loan.setNotes("Test Note: " + number);
        return loan;
    }

    public List<Loan> mockLoanEntities() {
        List<Loan> loans = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            loans.add(mockLoanEntity(i));
        }
        return loans;
    }

    public LoanResponseDTO mockLoanResponseDTO() {
        return mockLoanResponseDTO(1);
    }

    public LoanResponseDTO mockLoanResponseDTO(Integer number) {
        LoanResponseDTO dto = new LoanResponseDTO();
        dto.setId(number.longValue());
        dto.setStudentName("Student: " + number);
        dto.setBookName("Book: " + number);
        dto.setEmployeeName("Employee: " + number);
        dto.setLoanDate(LocalDate.of(2025, 8, 5));
        dto.setDueDate(LocalDate.of(2025, 8, 19));
        dto.setReturnDate(null);
        dto.setStatus(LoanStatus.ON_GOING);
        dto.setNotes("Test Note: " + number);
        return dto;
    }

    public List<LoanResponseDTO> mockLoanResponseDTOs() {
        List<LoanResponseDTO> dtos = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            dtos.add(mockLoanResponseDTO(i));
        }
        return dtos;
    }

    public LoanCreateDTO mockLoanCreateDTO() {
        LoanCreateDTO dto = new LoanCreateDTO();
        dto.setStudentId(1L);
        dto.setBookId(1L);
        return dto;
    }
}
