package com.coeus.books.services;

import com.coeus.books.exceptions.RequiredObjectIsNullException;
import com.coeus.books.models.Employee;
import com.coeus.books.models.dtos.EmployeeDTO;
import com.coeus.books.models.mapper.EmployeeMapper;
import com.coeus.books.repositories.EmployeeRepository;
import com.coeus.books.unittests.mocks.MockEmployee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    MockEmployee input;
    EmployeeService service;
    @Mock
    EmployeeRepository repository;
    EmployeeMapper mapper;

    @BeforeEach
    void setUp() {
        input = new MockEmployee();
        mapper = Mappers.getMapper(EmployeeMapper.class);
        service = new EmployeeService(repository, mapper);
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
                        && link.getHref().endsWith("/employees")
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
                        && link.getHref().endsWith("/employees")
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
                        && link.getHref().endsWith("/employees")
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
        List<Employee> list = input.mockEmployeeEntities();
        when(repository.findAll()).thenReturn(list);
        List<EmployeeDTO> dtos = service.findAll();

        assertNotNull(dtos);
        assertEquals(15, dtos.size());

        EmployeeDTO employeeOne = dtos.get(1);

        assertTrue(employeeOne.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("self")
                        && link.getHref().endsWith("/employees/1")
                        && link.getType().equals("GET")
                )
        );

        assertTrue(employeeOne.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("findAll")
                        && link.getHref().endsWith("/employees")
                        && link.getType().equals("GET")
                )
        );

        assertTrue(employeeOne.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create")
                        && link.getHref().endsWith("/employees")
                        && link.getType().equals("POST")
                )
        );

        assertTrue(employeeOne.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update")
                        && link.getHref().endsWith("/employees")
                        && link.getType().equals("PUT")
                )
        );

        assertTrue(employeeOne.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete")
                        && link.getHref().endsWith("/employees/1")
                        && link.getType().equals("DELETE")
                )
        );

        EmployeeDTO employeeSeven = dtos.get(7);

        assertTrue(employeeSeven.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("self")
                        && link.getHref().endsWith("/employees/7")
                        && link.getType().equals("GET")
                )
        );

        assertTrue(employeeSeven.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("findAll")
                        && link.getHref().endsWith("/employees")
                        && link.getType().equals("GET")
                )
        );

        assertTrue(employeeSeven.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create")
                        && link.getHref().endsWith("/employees")
                        && link.getType().equals("POST")
                )
        );

        assertTrue(employeeSeven.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update")
                        && link.getHref().endsWith("/employees")
                        && link.getType().equals("PUT")
                )
        );

        assertTrue(employeeSeven.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete")
                        && link.getHref().endsWith("/employees/7")
                        && link.getType().equals("DELETE")
                )
        );

        assertEquals("Employee register: 7", employeeSeven.getEmployeeRegister());
        assertEquals("Name: 7", employeeSeven.getName());
        assertEquals("Email: 7", employeeSeven.getEmail());

        EmployeeDTO employeeThirteen = dtos.get(13);

        assertTrue(employeeThirteen.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("self")
                        && link.getHref().endsWith("/employees/13")
                        && link.getType().equals("GET")
                )
        );

        assertTrue(employeeThirteen.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("findAll")
                        && link.getHref().endsWith("/employees")
                        && link.getType().equals("GET")
                )
        );

        assertTrue(employeeThirteen.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create")
                        && link.getHref().endsWith("/employees")
                        && link.getType().equals("POST")
                )
        );

        assertTrue(employeeThirteen.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update")
                        && link.getHref().endsWith("/employees")
                        && link.getType().equals("PUT")
                )
        );

        assertTrue(employeeThirteen.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete")
                        && link.getHref().endsWith("/employees/13")
                        && link.getType().equals("DELETE")
                )
        );

        assertEquals("Employee register: 13", employeeThirteen.getEmployeeRegister());
        assertEquals("Name: 13", employeeThirteen.getName());
        assertEquals("Email: 13", employeeThirteen.getEmail());
    }

}