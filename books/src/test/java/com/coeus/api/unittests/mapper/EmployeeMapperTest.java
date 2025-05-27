package com.coeus.api.unittests.mapper;

import com.coeus.api.models.Employee;
import com.coeus.api.models.dtos.EmployeeDTO;
import com.coeus.api.models.mapper.EmployeeMapper;
import com.coeus.api.unittests.mocks.MockEmployee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EmployeeMapperTest {

    MockEmployee mocker;
    EmployeeMapper mapper;

    @BeforeEach
    public void setUp() {
        mocker = new MockEmployee();
        mapper = Mappers.getMapper(EmployeeMapper.class);
    }

    @Test
    public void parseEntityToDTOTest() {
        EmployeeDTO employeeDTO = mapper.toDTO(mocker.mockEmployeeEntity());
        assertEquals("Employee register: 0", employeeDTO.getEmployeeRegister());
        assertEquals("Name: 0", employeeDTO.getName());
        assertEquals("Email: 0", employeeDTO.getEmail());
    }

    @Test
    public void parseDTOToEntityTest() {
        Employee employee = mapper.toEntity(mocker.mockEmployeeDTO());
        assertEquals("Employee register: 0", employee.getEmployeeRegister());
        assertEquals("Name: 0", employee.getName());
        assertEquals("Email: 0", employee.getEmail());
    }

    @Test
    public void parseEntitiesToDTOSTest() {
        List<EmployeeDTO> employeeDTOS = mapper.toDTOS(mocker.mockEmployeeEntities());

        EmployeeDTO employeeDTO2 = employeeDTOS.get(2);
        assertEquals("Employee register: 2", employeeDTO2.getEmployeeRegister());
        assertEquals("Name: 2", employeeDTO2.getName());
        assertEquals("Email: 2", employeeDTO2.getEmail());

        EmployeeDTO employeeDTO6 = employeeDTOS.get(6);
        assertEquals("Employee register: 6", employeeDTO6.getEmployeeRegister());
        assertEquals("Name: 6", employeeDTO6.getName());
        assertEquals("Email: 6", employeeDTO6.getEmail());

        EmployeeDTO employeeDTO11 = employeeDTOS.get(11);
        assertEquals("Employee register: 11", employeeDTO11.getEmployeeRegister());
        assertEquals("Name: 11", employeeDTO11.getName());
        assertEquals("Email: 11", employeeDTO11.getEmail());
    }

    @Test
    public void parseDTOSToEntitiesTest() {
        List<Employee> employees = mapper.toEntities(mocker.mockEmployeeDTOS());

        Employee employee2 = employees.get(2);
        assertEquals("Employee register: 2", employee2.getEmployeeRegister());
        assertEquals("Name: 2", employee2.getName());
        assertEquals("Email: 2", employee2.getEmail());

        Employee employee6 = employees.get(6);
        assertEquals("Employee register: 6", employee6.getEmployeeRegister());
        assertEquals("Name: 6", employee6.getName());
        assertEquals("Email: 6", employee6.getEmail());

        Employee employee11 = employees.get(11);
        assertEquals("Employee register: 11", employee11.getEmployeeRegister());
        assertEquals("Name: 11", employee11.getName());
        assertEquals("Email: 11", employee11.getEmail());
    }

}
