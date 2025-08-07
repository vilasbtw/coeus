package com.coeus.api.models.mapper;

import com.coeus.api.models.Warning;
import com.coeus.api.models.dtos.WarningDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/*
/   this class is responsible for mapping between Warning entity and WarningDTO.
/   it handles the conversion logic for:
/       - WarningDTO to Warning (input payload from POST /warnings)
/       - Warning to WarningDTO (API response payloads)
/
/   notes:
/   1. "student", "loan" and "employee" references will be resolved manually in the service layer.
/   2. fields like "id", "status", "issuedAt", and "resolvedAt" are managed by the backend.
*/

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface WarningMapper {

    // WarningDTO to Warning Entity (POST /warnings)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "student", ignore = true)
    @Mapping(target = "loan", ignore = true)
    @Mapping(target = "issuedBy", ignore = true)
    @Mapping(target = "status", ignore = true) // default = PENDING
    @Mapping(target = "issuedAt", ignore = true)
    @Mapping(target = "resolvedAt", ignore = true)
    Warning toEntity(WarningDTO dto);

    // Warning Entity to WarningDTO (GET /warnings)
    @Mapping(target = "studentId", source = "student.id")
    @Mapping(target = "loanId", source = "loan.id")
    @Mapping(target = "employeeId", source = "issuedBy.id")
    WarningDTO toDTO(Warning warning);

    // list of Warnings to list of WarningDTO
    List<WarningDTO> toDTOs(List<Warning> warnings);
}
