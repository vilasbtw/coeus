package com.coeus.api.services;

import com.coeus.api.controllers.LoanController;
import com.coeus.api.exceptions.*;
import com.coeus.api.models.Book;
import com.coeus.api.models.Employee;
import com.coeus.api.models.Loan;
import com.coeus.api.models.Student;
import com.coeus.api.models.dtos.LoanCreateDTO;
import com.coeus.api.models.dtos.LoanResponseDTO;
import com.coeus.api.models.enums.LoanStatus;
import com.coeus.api.models.mapper.LoanMapper;
import com.coeus.api.models.security.user.User;
import com.coeus.api.repositories.BookRepository;
import com.coeus.api.repositories.EmployeeRepository;
import com.coeus.api.repositories.LoanRepository;
import com.coeus.api.repositories.StudentRepository;
import com.coeus.api.repositories.security.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Service
public class LoanService {

    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final StudentRepository studentRepository;
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;

    private final LoanMapper loanMapper;
    private final PagedResourcesAssembler<LoanResponseDTO> assembler;

    @Autowired
    public LoanService(LoanRepository loanRepository, BookRepository bookRepository,
                       StudentRepository studentRepository, EmployeeRepository employeeRepository,
                       UserRepository userRepository, LoanMapper loanMapper,
                       PagedResourcesAssembler<LoanResponseDTO> assembler) {
        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
        this.studentRepository = studentRepository;
        this.employeeRepository = employeeRepository;
        this.userRepository = userRepository;
        this.loanMapper = loanMapper;
        this.assembler = assembler;
    }

    @Transactional
    public LoanResponseDTO create(LoanCreateDTO dto) {
        if (dto == null) throw new RequiredObjectIsNullException();

        Student student = studentRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        // check if the student is enabled to borrow books
        if (!student.isEnabled()) {
            throw new StudentNotEnabledException("Student is not enabled to borrow books");
        }

        Book book = bookRepository.findById(dto.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));
        // check if the book is available in stock
        if (book.getStock() <= 0) {
            throw new BookOutOfStockException("Book is out of stock");
        }

        // count how many active loans (ON_GOING or LATE) the student currently has
        // BUSINESS RULE: students can rent up to 3 books at a time
        long activeLoans = loanRepository.findByStudentId(student.getId()).stream()
                .filter(loan -> loan.getStatus() == LoanStatus.ON_GOING || loan.getStatus() == LoanStatus.LATE)
                .count();
        // validate if the student reached the limit of 3 active loans
        if (activeLoans >= 3) {
            throw new MaxLoansReachedException("Student has reached the maximum number of active loans");
        }
        // decrease this book stock by 1
        book.setStock(book.getStock() - 1);
        bookRepository.save(book);

        Loan loan = loanMapper.toEntity(dto);
        loan.setStudent(student);
        loan.setBook(book);
        loan.setEmployee(getAuthenticatedEmployee());

        // set the dueDate to exactly 14 days from now
        loan.setDueDate(LocalDate.now().plusDays(14));
        loan.setLoanDate(LocalDate.now());

        Loan persistedLoan = loanRepository.save(loan);
        LoanResponseDTO responseDTO = loanMapper.toDTO(persistedLoan);
        addHateoasLinks(responseDTO);

        return responseDTO;
    }

    @Transactional
    public LoanResponseDTO returnLoan(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found"));
        // check if the loan has already been returned
        if (loan.getStatus() == LoanStatus.RETURNED) {
            throw new LoanAlreadyReturnedException("Loan has already been returned");
        }

        // set the return date to today and update the loan status
        loan.setReturnDate(LocalDate.now());
        loan.setStatus(LoanStatus.RETURNED);

        // increase this book stock by 1
        Book book = loan.getBook();
        book.setStock(book.getStock() + 1);
        bookRepository.save(book);

        Loan persistedLoan = loanRepository.save(loan);
        LoanResponseDTO responseDTO = loanMapper.toDTO(persistedLoan);
        addHateoasLinks(responseDTO);

        return responseDTO;
    }


public PagedModel<EntityModel<LoanResponseDTO>> findByStudent(Long studentId, Pageable pageable) {
       // fetches loans by student id
       var loans = loanRepository.findByStudentId(studentId);
       var pagedDTOs = loans.stream()
               .map(loan -> {
                   LoanResponseDTO dto = loanMapper.toDTO(loan);
                   addHateoasLinks(dto);
                   return dto;
               })
               .toList();

       Link selfLink = linkTo(methodOn(LoanController.class)
           .findByStudent(studentId, pageable.getPageNumber(), pageable.getPageSize(), "asc"))
           .withSelfRel();

       return assembler.toModel(new org.springframework.data.domain.PageImpl<>(pagedDTOs, pageable, pagedDTOs.size()), selfLink);
   }

    public PagedModel<EntityModel<LoanResponseDTO>> findOverdue(Pageable pageable) {
        // fetches loans from their status
        LocalDate today = LocalDate.now();
        var overdueLoans = loanRepository.findByStatus(LoanStatus.ON_GOING).stream()
                .filter(loan -> loan.getDueDate().isBefore(today))
                .toList();

        var pagedDTOs = overdueLoans.stream()
                .map(loan -> {
                    LoanResponseDTO dto = loanMapper.toDTO(loan);
                    addHateoasLinks(dto);
                    return dto;
                })
                .toList();

        Link selfLink = linkTo(methodOn(LoanController.class).findOverdue(0, 12, "asc")).withSelfRel();
        return assembler.toModel(new org.springframework.data.domain.PageImpl<>(pagedDTOs, pageable, pagedDTOs.size()), selfLink);
    }

    private Employee getAuthenticatedEmployee() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;

        if (principal instanceof UserDetails userDetails) {
            username = userDetails.getUsername();
        } else {
            username = principal.toString();
        }

        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found"));

        return employeeRepository.findById(user.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for this user"));
    }

    private static void addHateoasLinks(LoanResponseDTO dto) {
        dto.add(linkTo(methodOn(LoanController.class).create(null)).withRel("create").withType("POST"));
        dto.add(linkTo(methodOn(LoanController.class).returnLoan(dto.getId())).withRel("return").withType("PATCH"));
        dto.add(linkTo(methodOn(LoanController.class).findByStudent(dto.getId(), 0, 12, "asc")).withRel("findByStudent").withType("GET"));
        dto.add(linkTo(methodOn(LoanController.class).findOverdue(0, 12, "asc")).withRel("findOverdue").withType("GET"));
    }
}