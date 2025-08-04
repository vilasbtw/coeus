package com.coeus.api.models.mapper;

import com.coeus.api.models.Loan;
import com.coeus.api.models.dtos.LoanCreateDTO;
import com.coeus.api.models.dtos.LoanResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

    /*
    /   this class is responsible for mapping between Loan entity and its corresponding DTOs.
    /   it handles the conversion logic for:
    /       - LoanCreateDTO to Loan (input payload by POST/loans)
    /       - Loan to LoanResponseDTO (API response payloads by GET/loans)
    /
    /   notes:
    /   1. the "employee" field will be set in the Service via SecurityContext, so it's ignored here.
    /   2. "loanDate", "status" and "returnDate" are managed by the backend and not required in LoanCreateDTO.
    */

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LoanMapper {

    // LoanCreateDTO to Loan Entity (POST /loans)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "student.id", source = "studentId")
    @Mapping(target = "book.id", source = "bookId")
    @Mapping(target = "employee", ignore = true) // set in service layer, via SecurityContext
    @Mapping(target = "loanDate", ignore = true)
    @Mapping(target = "returnDate", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "notes", ignore = true)
    Loan toEntity(LoanCreateDTO dto);

    // Loan Entity to LoanResponseDTO (GET /loans)
    @Mapping(target = "studentName", source = "student.name")
    @Mapping(target = "bookName", source = "book.bookName")
    @Mapping(target = "employeeName", source = "employee.name")
    LoanResponseDTO toDTO(Loan loan);

    // list of Loans to list of LoanResponseDTO
    List<LoanResponseDTO> toDTOs(List<Loan> loans);
}
