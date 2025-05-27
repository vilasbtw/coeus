package com.coeus.books.services;


import com.coeus.books.controllers.EmployeeController;
import com.coeus.books.exceptions.RequiredObjectIsNullException;
import com.coeus.books.exceptions.ResourceNotFoundException;
import com.coeus.books.models.Employee;
import com.coeus.books.models.dtos.EmployeeDTO;
import com.coeus.books.models.mapper.EmployeeMapper;
import com.coeus.books.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class EmployeeService {

    private final EmployeeRepository repository;
    private final EmployeeMapper mapper;

    @Autowired
    public EmployeeService(EmployeeRepository repository, EmployeeMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public EmployeeDTO create(EmployeeDTO employeeDTO) {

        if (employeeDTO == null) throw new RequiredObjectIsNullException();

        Employee entity = mapper.toEntity(employeeDTO);
        Employee persistedEmployee = repository.save(entity);
        EmployeeDTO dto = mapper.toDTO(persistedEmployee);
        addHateoasLinks(dto);
        return dto;
    }

    public EmployeeDTO findById(Long id) {
        Employee entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("This resource could not be found."));

        EmployeeDTO employeeDTO = mapper.toDTO(entity);
        addHateoasLinks(employeeDTO);
        return employeeDTO;
    }

    public List<EmployeeDTO> findAll() {
        List<Employee> employeesList = repository.findAll();
        List<EmployeeDTO> dtosList = new ArrayList<>();

        for (Employee employee : employeesList) {
            dtosList.add(mapper.toDTO(employee));
        }

        for (EmployeeDTO dto : dtosList) {
            addHateoasLinks(dto);
        }
        return dtosList;
    }

    public EmployeeDTO update(EmployeeDTO employeeDTO) {

        if (employeeDTO == null) throw new RequiredObjectIsNullException();

        Employee entity = repository.findById(employeeDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundException("This resource could not be found."));

        entity.setEmployeeRegister(employeeDTO.getEmployeeRegister());
        entity.setName(employeeDTO.getName());
        entity.setEmail(employeeDTO.getEmail());

        repository.save(entity);
        EmployeeDTO dto = mapper.toDTO(entity);
        addHateoasLinks(dto);
        return dto;
    }

    public void delete(Long id) {
        Employee entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("This resource could not be found."));

        repository.delete(entity);
    }

    private static void addHateoasLinks(EmployeeDTO employeeDTO) {
        employeeDTO.add(linkTo(methodOn(EmployeeController.class).create(employeeDTO)).withRel("create").withType("POST"));
        employeeDTO.add(linkTo(methodOn(EmployeeController.class).findById(employeeDTO.getId())).withSelfRel().withType("GET"));
        employeeDTO.add(linkTo(methodOn(EmployeeController.class).findAll()).withRel("findAll").withType("GET"));
        employeeDTO.add(linkTo(methodOn(EmployeeController.class).update(employeeDTO)).withRel("update").withType("PUT"));
        employeeDTO.add(linkTo(methodOn(EmployeeController.class).delete(employeeDTO.getId())).withRel("delete").withType("DELETE"));
    }
}