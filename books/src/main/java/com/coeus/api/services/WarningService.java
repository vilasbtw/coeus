package com.coeus.api.services;

import com.coeus.api.controllers.WarningController;
import com.coeus.api.exceptions.*;
import com.coeus.api.models.*;
import com.coeus.api.models.dtos.WarningDTO;
import com.coeus.api.models.enums.WarningStatus;
import com.coeus.api.models.mapper.WarningMapper;
import com.coeus.api.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalDate;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Service
public class WarningService {

    private final WarningRepository repository;
    private final WarningMapper mapper;
    private final StudentRepository studentRepository;
    private final LoanRepository loanRepository;
    private final EmployeeRepository employeeRepository;
    private final PagedResourcesAssembler<WarningDTO> assembler;

    @Autowired
    public WarningService(WarningRepository repository,
                          WarningMapper mapper,
                          StudentRepository studentRepository,
                          LoanRepository loanRepository,
                          EmployeeRepository employeeRepository,
                          PagedResourcesAssembler<WarningDTO> assembler) {
        this.repository = repository;
        this.mapper = mapper;
        this.studentRepository = studentRepository;
        this.loanRepository = loanRepository;
        this.employeeRepository = employeeRepository;
        this.assembler = assembler;
    }

    @Transactional
    public WarningDTO create(WarningDTO dto) {
        if (dto == null) throw new RequiredObjectIsNullException();

        Student student = studentRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        Loan loan = null;
        if (dto.getLoanId() != null) {
            loan = loanRepository.findById(dto.getLoanId())
                    .orElseThrow(() -> new LoanNotFoundException(dto.getLoanId()));

            if (repository.existsByLoanId(dto.getLoanId())) {
                throw new DuplicateWarningForLoanException(dto.getLoanId());
            }
        }

        Warning entity = mapper.toEntity(dto);
        entity.setStudent(student);
        entity.setIssuedBy(employee);
        entity.setLoan(loan);
        entity.setStatus(WarningStatus.PENDING);
        entity.setIssuedAt(LocalDateTime.now());

        Warning persisted = repository.save(entity);
        WarningDTO response = mapper.toDTO(persisted);
        addHateoasLinks(response);
        return response;
    }

    @Transactional
    public WarningDTO resolve(Long id) {
        Warning warning = repository.findById(id)
                .orElseThrow(() -> new WarningNotFoundException(id));

        warning.setStatus(WarningStatus.RESOLVED);
        warning.setResolvedAt(LocalDateTime.now());

        Warning persisted = repository.save(warning);
        WarningDTO dto = mapper.toDTO(persisted);
        addHateoasLinks(dto);
        return dto;
    }

    @Transactional
    public void delete(Long id) {
        Warning warning = repository.findById(id)
                .orElseThrow(() -> new WarningNotFoundException(id));

        // TODO: Add role check in Controller before calling this method (LIBRARIAN or COORDINATOR)
        repository.delete(warning);
    }

    public WarningDTO findById(Long id) {
        Warning warning = repository.findById(id)
                .orElseThrow(() -> new WarningNotFoundException(id));

        WarningDTO dto = mapper.toDTO(warning);
        addHateoasLinks(dto);
        return dto;
    }

    public PagedModel<EntityModel<WarningDTO>> findAll(Pageable pageable) {
        Page<Warning> warnings = repository.findAll(pageable);
        Page<WarningDTO> dtoPage = warnings.map(warning -> {
            WarningDTO dto = mapper.toDTO(warning);
            addHateoasLinks(dto);
            return dto;
        });

        Link selfLink = linkTo(methodOn(WarningController.class)
                .findAll(pageable.getPageNumber(), pageable.getPageSize(), "asc"))
                .withSelfRel();

        return assembler.toModel(dtoPage, selfLink);
    }

    public PagedModel<EntityModel<WarningDTO>> findAllByStudent(Long studentId, Pageable pageable) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        var dtoList = repository.findByStudentId(studentId).stream()
                .map(warning -> {
                    WarningDTO dto = mapper.toDTO(warning);
                    addHateoasLinks(dto);
                    return dto;
                }).toList();

        Link selfLink = linkTo(methodOn(WarningController.class)
                .findByStudent(studentId, pageable.getPageNumber(), pageable.getPageSize(), "asc"))
                .withSelfRel();

        return assembler.toModel(new PageImpl<>(dtoList, pageable, dtoList.size()), selfLink);
    }

    @Transactional
    public void generateWarningForOverdueLoan(Loan loan) {
        if (repository.existsByLoanId(loan.getId())) return;

        Student student = loan.getStudent();
        Employee system = loan.getEmployee();

        String reason;
        if (loan.getReturnDate() != null && loan.getReturnDate().isAfter(loan.getDueDate())) {
            reason = "Book returned after due date.";
        } else if (loan.getReturnDate() == null && LocalDate.now().isAfter(loan.getDueDate().plusDays(3))) {
            reason = "Book not returned within 3 days after due date.";
        } else {
            return;
        }

        Warning warning = new Warning();
        warning.setStudent(student);
        warning.setLoan(loan);
        warning.setIssuedBy(system);
        warning.setReason(reason);
        warning.setStatus(WarningStatus.PENDING);
        warning.setIssuedAt(LocalDateTime.now());

        repository.save(warning);
    }

    public void validateStudentRestrictions(Long studentId) {
        boolean hasPending = repository.existsByStudentIdAndStatus(studentId, WarningStatus.PENDING);
        if (hasPending) {
            throw new StudentWithPendingWarningsException(studentId);
        }
    }

    private static void addHateoasLinks(WarningDTO dto) {
        dto.add(linkTo(methodOn(WarningController.class).create(null)).withRel("create").withType("POST"));
        dto.add(linkTo(methodOn(WarningController.class).resolve(dto.getId())).withRel("resolve").withType("PUT"));
        dto.add(linkTo(methodOn(WarningController.class).delete(dto.getId())).withRel("delete").withType("DELETE"));
        dto.add(linkTo(methodOn(WarningController.class).findByStudent(dto.getStudentId(), 0, 12, "asc")).withRel("findByStudent").withType("GET"));
        dto.add(linkTo(methodOn(WarningController.class).findAll(0, 12, "asc")).withRel("findAll").withType("GET"));
    }
}
