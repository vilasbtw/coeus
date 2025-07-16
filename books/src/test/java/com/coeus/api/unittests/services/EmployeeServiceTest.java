package com.coeus.api.unittests.services;

import com.coeus.api.exceptions.RequiredObjectIsNullException;
import com.coeus.api.models.Book;
import com.coeus.api.models.Employee;
import com.coeus.api.models.dtos.BookDTO;
import com.coeus.api.models.dtos.EmployeeDTO;
import com.coeus.api.models.mapper.EmployeeMapper;
import com.coeus.api.repositories.EmployeeRepository;
import com.coeus.api.services.EmployeeService;
import com.coeus.api.unittests.mocks.MockEmployee;
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
class EmployeeServiceTest {

    MockEmployee input;
    EmployeeService service;
    @Mock
    EmployeeRepository repository;
    EmployeeMapper mapper;

    @Mock
    PagedResourcesAssembler<EmployeeDTO> assembler;

    @BeforeEach
    void setUp() {
        input = new MockEmployee();
        mapper = Mappers.getMapper(EmployeeMapper.class);
        service = new EmployeeService(repository, mapper, assembler);
    }

    @Test
    void findById() {
        Employee employee = input.mockEmployeeEntity(1);
        when(repository.findById(1L)).thenReturn(Optional.of(employee));

        EmployeeDTO result = service.findById(1L);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("self")
                        && link.getHref().endsWith("/employees/1")
                        && link.getType().equals("GET")
                )
        );

        assertTrue(result.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("findAll")
                    && link.getHref().contains("/employees")
                    && link.getType().equals("GET")
            )
        );

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create")
                        && link.getHref().endsWith("/employees")
                        && link.getType().equals("POST")
                )
        );

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update")
                        && link.getHref().endsWith("/employees")
                        && link.getType().equals("PUT")
                )
        );

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete")
                        && link.getHref().endsWith("/employees/1")
                        && link.getType().equals("DELETE")
                )
        );

        assertEquals("Employee register: 1", employee.getEmployeeRegister());
        assertEquals("Name: 1", employee.getName());
        assertEquals("Email: 1", employee.getEmail());
    }

    @Test
    void testCreateWithNullEmployee() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class,
                () -> {
                    service.create(null);
                });

        String expectedMessage = "It is not allowed to persist a null object!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void create() {
        Employee employee = input.mockEmployeeEntity(1);
        Employee persisted = employee;

        EmployeeDTO dto = input.mockEmployeeDTO(1);

        when(repository.save(employee)).thenReturn(persisted);

        EmployeeDTO result = service.create(dto);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("self")
                        && link.getHref().endsWith("/employees/1")
                        && link.getType().equals("GET")
                )
        );

        assertTrue(result.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("findAll")
                    && link.getHref().contains("/employees")
                    && link.getType().equals("GET")
            )
        );

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create")
                        && link.getHref().endsWith("/employees")
                        && link.getType().equals("POST")
                )
        );

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update")
                        && link.getHref().endsWith("/employees")
                        && link.getType().equals("PUT")
                )
        );

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete")
                        && link.getHref().endsWith("/employees/1")
                        && link.getType().equals("DELETE")
                )
        );

        assertEquals("Employee register: 1", employee.getEmployeeRegister());
        assertEquals("Name: 1", employee.getName());
        assertEquals("Email: 1", employee.getEmail());
    }

    @Test
    void testUpdateWithNullEmployee() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class,
                () -> {
                    service.update(null);
                });

        String expectedMessage = "It is not allowed to persist a null object!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void update() {
        Employee employee = input.mockEmployeeEntity(1);
        Employee persisted = employee;

        EmployeeDTO dto = input.mockEmployeeDTO(1);

        when(repository.findById(1L)).thenReturn(Optional.of(employee));
        when(repository.save(employee)).thenReturn(persisted);

        EmployeeDTO result = service.update(dto);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("self")
                        && link.getHref().endsWith("/employees/1")
                        && link.getType().equals("GET")
                )
        );

        assertTrue(result.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("findAll")
                    && link.getHref().contains("/employees")
                    && link.getType().equals("GET")
            )
        );

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create")
                        && link.getHref().endsWith("/employees")
                        && link.getType().equals("POST")
                )
        );

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update")
                        && link.getHref().endsWith("/employees")
                        && link.getType().equals("PUT")
                )
        );

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete")
                        && link.getHref().endsWith("/employees/1")
                        && link.getType().equals("DELETE")
                )
        );

        assertEquals("Employee register: 1", employee.getEmployeeRegister());
        assertEquals("Name: 1", employee.getName());
        assertEquals("Email: 1", employee.getEmail());
    }

    @Test
    void delete() {
        Employee employee = input.mockEmployeeEntity(1);
        when(repository.findById(1L)).thenReturn(Optional.of(employee));

        service.delete(1L);
    }

    @Test
    void findAll() {
        List<Employee> entityList = input.mockEmployeeEntities();
        Page<Employee> entityPage = new PageImpl<>(entityList);
        when(repository.findAll(any(Pageable.class))).thenReturn(entityPage);

        when(assembler.toModel(any(Page.class), any(Link.class))).thenAnswer(invocation -> {
            Page<EmployeeDTO> dtoPage = invocation.getArgument(0);
            List<EntityModel<EmployeeDTO>> models = dtoPage.getContent().stream()
                    .map(EntityModel::of)
                    .collect(Collectors.toList());
            PagedModel.PageMetadata metadata = new PagedModel.PageMetadata(
                dtoPage.getSize(),
                dtoPage.getNumber(),
                dtoPage.getTotalElements()
            );
            return PagedModel.of(models, metadata);
        });

        Pageable pageable = PageRequest.of(0, 15);

        PagedModel<EntityModel<EmployeeDTO>> result = service.findAll(pageable);

        assertNotNull(result);
        List<EmployeeDTO> employees = result.getContent().stream()
                .map(EntityModel::getContent)
                .collect(Collectors.toList());

        assertNotNull(employees);
        assertEquals(15, employees.size());

        validateIndividualEmployee(employees.get(1), 1);
        validateIndividualEmployee(employees.get(5), 5);
        validateIndividualEmployee(employees.get(10), 10);
    }

    private void validateIndividualEmployee(EmployeeDTO employee, int i) {
        assertEquals("Employee register: " + i, employee.getEmployeeRegister());
        assertEquals("Name: " + i, employee.getName());
        assertEquals("Email: " + i, employee.getEmail());

        assertNotNull(employee);
        assertNotNull(employee.getId());
        assertNotNull(employee.getLinks());

        assertTrue(employee.getLink("self").isPresent());
        assertTrue(employee.getLink("self").get().getHref().endsWith("/employees/" + i));

        assertTrue(employee.getLink("findAll").isPresent());
        assertTrue(employee.getLink("findAll").get().getHref().contains("/employees"));

        assertTrue(employee.getLink("create").isPresent());
        assertTrue(employee.getLink("create").get().getHref().endsWith("/employees"));

        assertTrue(employee.getLink("update").isPresent());
        assertTrue(employee.getLink("update").get().getHref().endsWith("/employees"));

        assertTrue(employee.getLink("delete").isPresent());
        assertTrue(employee.getLink("delete").get().getHref().endsWith("/employees/" + i));
    }

}