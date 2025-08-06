package com.coeus.api.unittests.services;

import com.coeus.api.models.Loan;
import com.coeus.api.models.dtos.LoanCreateDTO;
import com.coeus.api.models.dtos.LoanResponseDTO;
import com.coeus.api.models.enums.LoanStatus;
import com.coeus.api.models.mapper.LoanMapper;
import com.coeus.api.repositories.*;
import com.coeus.api.repositories.security.UserRepository;
import com.coeus.api.services.LoanService;
import com.coeus.api.unittests.mocks.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class LoanServiceTest {

    MockLoan input;
    MockUser mockUser;

    @Mock
    LoanRepository loanRepository;
    @Mock
    BookRepository bookRepository;
    @Mock
    StudentRepository studentRepository;
    @Mock
    EmployeeRepository employeeRepository;
    @Mock
    UserRepository userRepository;

    @Mock
    PagedResourcesAssembler<LoanResponseDTO> assembler;

    LoanMapper mapper;

    @InjectMocks
    LoanService service;

    @BeforeEach
    void setUp() {
        input = new MockLoan();
        mockUser = new MockUser();
        mapper = Mappers.getMapper(LoanMapper.class);
        service = new LoanService(loanRepository, bookRepository, studentRepository, employeeRepository, userRepository, mapper, assembler);
    }

    @Test
    void createLoanTest() {
        // mocking the SecurityContextHolder for the authentication
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn("chorao");
        SecurityContextHolder.setContext(securityContext);

        LoanCreateDTO createDTO = input.mockLoanCreateDTO();
        Loan entity = input.mockLoanEntity(1);

        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(entity.getStudent()));
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(entity.getBook()));
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(mockUser.mockUserEntity(1L)));
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(entity.getEmployee()));
        when(loanRepository.save(any(Loan.class))).thenReturn(entity);

        LoanResponseDTO result = service.create(createDTO);

        assertNotNull(result);
        assertEquals("Name: 1", result.getStudentName());
        assertEquals("Book name: 1", result.getBookName());
        assertEquals("Name: 1", result.getEmployeeName());
        assertEquals(LoanStatus.ON_GOING, result.getStatus());
        assertNotNull(result.getLinks());
    }

    @Test
    void returnLoanTest() {
        Loan entity = input.mockLoanEntity(1);

        when(loanRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(loanRepository.save(any(Loan.class))).thenReturn(entity);

        LoanResponseDTO result = service.returnLoan(1L);

        assertNotNull(result);
        assertEquals(LoanStatus.RETURNED, result.getStatus());
        assertNotNull(result.getReturnDate());
    }

    @Test
    void findByStudentTest() {
        List<Loan> loans = input.mockLoanEntities();
        when(loanRepository.findByStudentId(anyLong())).thenReturn(loans);

        when(assembler.toModel(any(Page.class), any(Link.class))).thenAnswer(invocation -> {
            Page<LoanResponseDTO> dtoPage = invocation.getArgument(0);
            List<EntityModel<LoanResponseDTO>> models = dtoPage.getContent().stream()
                    .map(EntityModel::of)
                    .collect(Collectors.toList());
            PagedModel.PageMetadata metadata = new PagedModel.PageMetadata(
                dtoPage.getSize(),
                dtoPage.getNumber(),
                dtoPage.getTotalElements()
            );
            return PagedModel.of(models, metadata);
        });

        Pageable pageable = PageRequest.of(0, 10);

        PagedModel<EntityModel<LoanResponseDTO>> result = service.findByStudent(1L, pageable);

        assertNotNull(result);
        assertEquals(10, result.getContent().size());
    }

    @Test
    void findOverdueTest() {
        List<Loan> loans = input.mockLoanEntities();
        loans.forEach(loan -> loan.setDueDate(loan.getDueDate().minusDays(30))); // simulate overdue

        when(loanRepository.findByStatus(LoanStatus.ON_GOING)).thenReturn(loans);

        when(assembler.toModel(any(Page.class), any(Link.class))).thenAnswer(invocation -> {
            Page<LoanResponseDTO> dtoPage = invocation.getArgument(0);
            List<EntityModel<LoanResponseDTO>> models = dtoPage.getContent().stream()
                    .map(EntityModel::of)
                    .collect(Collectors.toList());
            PagedModel.PageMetadata metadata = new PagedModel.PageMetadata(
                dtoPage.getSize(),
                dtoPage.getNumber(),
                dtoPage.getTotalElements()
            );
            return PagedModel.of(models, metadata);
        });

        Pageable pageable = PageRequest.of(0, 10);

        PagedModel<EntityModel<LoanResponseDTO>> result = service.findOverdue(pageable);

        assertNotNull(result);
        assertTrue(result.getContent().size() > 0);
    }
}
