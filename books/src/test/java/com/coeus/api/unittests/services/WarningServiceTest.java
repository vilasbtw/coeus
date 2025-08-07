package com.coeus.api.unittests.services;

import com.coeus.api.exceptions.RequiredObjectIsNullException;
import com.coeus.api.models.Warning;
import com.coeus.api.models.dtos.WarningDTO;
import com.coeus.api.models.enums.WarningStatus;
import com.coeus.api.models.mapper.WarningMapper;
import com.coeus.api.repositories.*;
import com.coeus.api.services.WarningService;
import com.coeus.api.unittests.mocks.MockWarning;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class WarningServiceTest {

    MockWarning input;
    WarningService service;

    @Mock WarningRepository repository;
    @Mock StudentRepository studentRepository;
    @Mock LoanRepository loanRepository;
    @Mock EmployeeRepository employeeRepository;
    @Mock PagedResourcesAssembler<WarningDTO> assembler;

    WarningMapper mapper;

    @BeforeEach
    void setUp() {
        input = new MockWarning();
        mapper = Mappers.getMapper(WarningMapper.class);
        service = new WarningService(repository, mapper, studentRepository, loanRepository, employeeRepository, assembler);
    }

    @Test
    void findById() {
        Warning warning = input.mockWarningEntity(1);
        when(repository.findById(1L)).thenReturn(Optional.of(warning));

        WarningDTO result = service.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Reason: 1", result.getReason());
        assertEquals("Details for warning 1", result.getDetails());
        assertEquals(WarningStatus.PENDING, result.getStatus());
        assertNotNull(result.getLinks());
        assertTrue(result.getLink("delete").isPresent());
        assertTrue(result.getLink("resolve").isPresent());
        assertTrue(result.getLink("findAll").isPresent());
    }

    @Test
    void create() {
        WarningDTO dto = input.mockWarningDTO(1);
        Warning entity = input.mockWarningEntity(1);

        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(entity.getStudent()));
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(entity.getIssuedBy()));
        when(loanRepository.findById(anyLong())).thenReturn(Optional.of(entity.getLoan()));
        when(repository.existsByLoanId(anyLong())).thenReturn(false);
        when(repository.save(any(Warning.class))).thenReturn(entity);

        WarningDTO result = service.create(dto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Reason: 1", result.getReason());
        assertEquals("Details for warning 1", result.getDetails());
        assertEquals(WarningStatus.PENDING, result.getStatus());
        assertNotNull(result.getLinks());
        assertTrue(result.getLink("delete").isPresent());
        assertTrue(result.getLink("resolve").isPresent());
        assertTrue(result.getLink("findAll").isPresent());
    }

    @Test
    void testCreateWithNullWarning() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class,
                () -> service.create(null));

        String expectedMessage = "It is not allowed to persist a null object!";
        assertTrue(exception.getMessage().contains(expectedMessage));
    }

    @Test
    void resolve() {
        Warning entity = input.mockWarningEntity(1);
        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(repository.save(any(Warning.class))).thenReturn(entity);

        WarningDTO result = service.resolve(1L);

        assertNotNull(result);
        assertEquals(WarningStatus.RESOLVED, result.getStatus());
        assertNotNull(result.getResolvedAt());
    }

    @Test
    void delete() {
        Warning entity = input.mockWarningEntity(1);
        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        service.delete(1L);
        verify(repository, times(1)).delete(entity);
    }

    @Test
    void findAll() {
        List<Warning> entities = input.mockWarningEntities();
        Page<Warning> page = new PageImpl<>(entities);

        when(repository.findAll(any(Pageable.class))).thenReturn(page);
        when(assembler.toModel(any(Page.class), any(Link.class))).thenAnswer(invocation -> {
            Page<WarningDTO> dtoPage = invocation.getArgument(0);
            List<EntityModel<WarningDTO>> models = dtoPage.getContent().stream()
                    .map(EntityModel::of)
                    .collect(Collectors.toList());
            PagedModel.PageMetadata metadata = new PagedModel.PageMetadata(
                    dtoPage.getSize(), dtoPage.getNumber(), dtoPage.getTotalElements()
            );
            return PagedModel.of(models, metadata);
        });

        Pageable pageable = PageRequest.of(0, 10);
        PagedModel<EntityModel<WarningDTO>> result = service.findAll(pageable);

        assertNotNull(result);
        assertEquals(10, result.getContent().size());
    }

    @Test
    void findAllByStudent() {
        List<Warning> entities = input.mockWarningEntities();
        Warning entity = input.mockWarningEntity(1);

        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(entity.getStudent()));
        when(repository.findByStudentId(anyLong())).thenReturn(entities);
        when(assembler.toModel(any(Page.class), any(Link.class))).thenAnswer(invocation -> {
            Page<WarningDTO> dtoPage = invocation.getArgument(0);
            List<EntityModel<WarningDTO>> models = dtoPage.getContent().stream()
                    .map(EntityModel::of)
                    .collect(Collectors.toList());
            PagedModel.PageMetadata metadata = new PagedModel.PageMetadata(
                    dtoPage.getSize(), dtoPage.getNumber(), dtoPage.getTotalElements()
            );
            return PagedModel.of(models, metadata);
        });

        Pageable pageable = PageRequest.of(0, 10);
        PagedModel<EntityModel<WarningDTO>> result = service.findAllByStudent(1L, pageable);

        assertNotNull(result);
        assertEquals(10, result.getContent().size());
    }
}
