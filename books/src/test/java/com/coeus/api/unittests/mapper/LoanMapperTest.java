package com.coeus.api.unittests.mapper;

import com.coeus.api.models.Loan;
import com.coeus.api.models.dtos.LoanResponseDTO;
import com.coeus.api.models.mapper.LoanMapper;
import com.coeus.api.unittests.mocks.MockLoan;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LoanMapperTest {

    MockLoan mocker;
    LoanMapper mapper;

    @BeforeEach
    public void setUp() {
        mocker = new MockLoan();
        mapper = Mappers.getMapper(LoanMapper.class);
    }

    @Test
    public void parseEntityToDTOTest() {
        LoanResponseDTO dto = mapper.toDTO(mocker.mockLoanEntity(1));
        assertEquals(1L, dto.getId());
        assertEquals("Name: 1", dto.getStudentName());
        assertEquals("Book name: 1", dto.getBookName());
        assertEquals("Name: 1", dto.getEmployeeName());
        assertEquals("Test Note: 1", dto.getNotes());
    }

    @Test
    public void parseEntitiesToDTOSTest() {
        List<Loan> loans = mocker.mockLoanEntities();
        List<LoanResponseDTO> dtos = mapper.toDTOs(loans);

        assertEquals(loans.size(), dtos.size());

        LoanResponseDTO dto2 = dtos.get(2);
        assertEquals(2L, dto2.getId());
        assertEquals("Name: 2", dto2.getStudentName());
        assertEquals("Book name: 2", dto2.getBookName());
        assertEquals("Name: 2", dto2.getEmployeeName());

        LoanResponseDTO dto5 = dtos.get(5);
        assertEquals(5L, dto5.getId());
        assertEquals("Name: 5", dto5.getStudentName());
        assertEquals("Book name: 5", dto5.getBookName());
        assertEquals("Name: 5", dto5.getEmployeeName());
    }
}
