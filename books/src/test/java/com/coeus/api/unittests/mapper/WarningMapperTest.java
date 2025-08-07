package com.coeus.api.unittests.mapper;

import com.coeus.api.models.Warning;
import com.coeus.api.models.dtos.WarningDTO;
import com.coeus.api.models.enums.WarningStatus;
import com.coeus.api.models.mapper.WarningMapper;
import com.coeus.api.unittests.mocks.MockWarning;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WarningMapperTest {

    MockWarning mocker;
    WarningMapper mapper;

    @BeforeEach
    public void setUp() {
        mocker = new MockWarning();
        mapper = Mappers.getMapper(WarningMapper.class);
    }

    @Test
    public void parseEntityToDTOTest() {
        WarningDTO dto = mapper.toDTO(mocker.mockWarningEntity());

        assertEquals(1L, dto.getId());
        assertEquals(1L, dto.getStudentId());
        assertEquals(1L, dto.getLoanId());
        assertEquals(1L, dto.getEmployeeId());
        assertEquals("Reason: 1", dto.getReason());
        assertEquals("Details for warning 1", dto.getDetails());
        assertEquals(WarningStatus.PENDING, dto.getStatus());
        assertEquals(LocalDateTime.of(2025, 8, 7, 10, 0), dto.getIssuedAt());
    }

    @Test
    public void parseDTOToEntityTest() {
        Warning entity = mapper.toEntity(mocker.mockWarningDTO());

        assertEquals("Reason: 1", entity.getReason());
        assertEquals("Details for warning 1", entity.getDetails());
    }

    @Test
    public void parseEntitiesToDTOSTest() {
        List<Warning> entities = mocker.mockWarningEntities();
        List<WarningDTO> dtos = mapper.toDTOs(entities);

        WarningDTO dto2 = dtos.get(2);
        assertEquals(2L, dto2.getId());
        assertEquals(2L, dto2.getStudentId());
        assertEquals(2L, dto2.getLoanId());
        assertEquals(2L, dto2.getEmployeeId());
        assertEquals("Reason: 2", dto2.getReason());

        WarningDTO dto6 = dtos.get(6);
        assertEquals(6L, dto6.getId());
        assertEquals(6L, dto6.getStudentId());
        assertEquals(6L, dto6.getLoanId());
        assertEquals(6L, dto6.getEmployeeId());
        assertEquals("Reason: 6", dto6.getReason());

        WarningDTO dto9 = dtos.get(9);
        assertEquals(9L, dto9.getId());
        assertEquals(9L, dto9.getStudentId());
        assertEquals(9L, dto9.getLoanId());
        assertEquals(9L, dto9.getEmployeeId());
        assertEquals("Reason: 9", dto9.getReason());
    }
}
