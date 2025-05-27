package com.coeus.api.unittests.mocks;

import com.coeus.api.models.Employee;
import com.coeus.api.models.dtos.EmployeeDTO;

import java.util.ArrayList;
import java.util.List;

public class MockEmployee {

    public Employee mockEmployeeEntity() {
        return mockEmployeeEntity(0);
    }

    public Employee mockEmployeeEntity(Integer number) {
        Employee employee = new Employee();

        employee.setId(number.longValue());
        employee.setEmployeeRegister("Employee register: " + number);
        employee.setName("Name: " + number);
        employee.setEmail("Email: " + number);
        return employee;
    }

    public List<Employee> mockEmployeeEntities() {
        List<Employee> employees = new ArrayList<>();

        for (int i = 0; i < 15; i++) {
            employees.add(mockEmployeeEntity(i));
        }
        return employees;
    }

    public EmployeeDTO mockEmployeeDTO() {
        return mockEmployeeDTO(0);
    }

    public EmployeeDTO mockEmployeeDTO(Integer number) {
        EmployeeDTO employeeDTO = new EmployeeDTO();

        employeeDTO.setId(number.longValue());
        employeeDTO.setEmployeeRegister("Employee register: " + number);
        employeeDTO.setName("Name: " + number);
        employeeDTO.setEmail("Email: " + number);
        return employeeDTO;
    }

    public List<EmployeeDTO> mockEmployeeDTOS() {
        List<EmployeeDTO> employeeDTOS = new ArrayList<>();

        for (int i = 0; i < 15; i++) {
            employeeDTOS.add(mockEmployeeDTO(i));
        }
        return employeeDTOS;
    }

}