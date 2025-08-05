package com.coeus.api.controllers;

import com.coeus.api.controllers.docs.LoanControllerDocs;
import com.coeus.api.models.dtos.LoanCreateDTO;
import com.coeus.api.models.dtos.LoanResponseDTO;
import com.coeus.api.services.LoanService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/loans")
@Tag(name = "Loans", description = "Endpoints for managing book loans")
public class LoanController implements LoanControllerDocs {

    @Autowired
    private LoanService service;

    @PostMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public LoanResponseDTO create(@RequestBody LoanCreateDTO loanCreateDTO) {
        return service.create(loanCreateDTO);
    }

    @PatchMapping(value = "/{id}/return",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public LoanResponseDTO returnLoan(@PathVariable(value = "id") Long id) {
        return service.returnLoan(id);
    }

    @GetMapping(value = "/student/{studentId}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public ResponseEntity<PagedModel<EntityModel<LoanResponseDTO>>> findByStudent(
            @PathVariable(value = "studentId") Long studentId,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "12") Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction
    ) {
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "dueDate"));
        return ResponseEntity.ok(service.findByStudent(studentId, pageable));
    }

    @GetMapping(value = "/overdue",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public ResponseEntity<PagedModel<EntityModel<LoanResponseDTO>>> findOverdue(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "12") Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction
    ) {
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "dueDate"));
        return ResponseEntity.ok(service.findOverdue(pageable));
    }
}
