package com.coeus.api.services;

import com.coeus.api.controllers.EmployeeController;
import com.coeus.api.exceptions.RequiredObjectIsNullException;
import com.coeus.api.exceptions.ResourceNotFoundException;
import com.coeus.api.models.Employee;
import com.coeus.api.models.dtos.EmployeeDTO;
import com.coeus.api.models.mapper.EmployeeMapper;
import com.coeus.api.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class EmployeeService {

    private final EmployeeRepository repository;
    private final EmployeeMapper mapper;

    // converts paginated EmployeeDTOs into a response with HATEOAS links, e.g.: "next", "previous", "self", "last"...
    PagedResourcesAssembler<EmployeeDTO> assembler;

    @Autowired
    public EmployeeService(EmployeeRepository repository, EmployeeMapper mapper, PagedResourcesAssembler<EmployeeDTO> assembler) {
        this.repository = repository;
        this.mapper = mapper;
        this.assembler = assembler;
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

    public PagedModel<EntityModel<EmployeeDTO>> findAll(Pageable pageable) {
        var employees = repository.findAll(pageable);
        var pagedDTOs = employees.map(employee -> {
            EmployeeDTO dto = mapper.toDTO(employee);
            addHateoasLinks(dto);
            return dto;
        });

        // adding HATEOAS "self" links for the current page requested by the client.
        Link findAllLink = WebMvcLinkBuilder.linkTo(
            WebMvcLinkBuilder.methodOn(EmployeeController.class)
                .findAll(
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    String.valueOf(pageable.getSort())
                )
        ).withSelfRel();

        return assembler.toModel(pagedDTOs, findAllLink);
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
        employeeDTO.add(linkTo(methodOn(EmployeeController.class).findAll(1, 12, "asc")).withRel("findAll").withType("GET"));
        employeeDTO.add(linkTo(methodOn(EmployeeController.class).update(employeeDTO)).withRel("update").withType("PUT"));
        employeeDTO.add(linkTo(methodOn(EmployeeController.class).delete(employeeDTO.getId())).withRel("delete").withType("DELETE"));
    }
}