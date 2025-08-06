package com.coeus.api.integrationtests.controllers.withjson;

import com.coeus.api.config.TestConfigs;
import com.coeus.api.integrationtests.dto.AuthenticationDTO;
import com.coeus.api.integrationtests.dto.LoanCreateDTO;
import com.coeus.api.integrationtests.dto.LoanResponseDTO;
import com.coeus.api.integrationtests.dto.TokenDTO;
import com.coeus.api.integrationtests.dto.wrapper.json.WrapperLoanDTO;
import com.coeus.api.integrationtests.testcontainers.AbstractIntegrationTest;
import com.coeus.api.models.Loan;
import com.coeus.api.models.enums.LoanStatus;
import com.coeus.api.models.security.user.User;
import com.coeus.api.models.security.user.UserRole;
import com.coeus.api.repositories.BookRepository;
import com.coeus.api.repositories.EmployeeRepository;
import com.coeus.api.repositories.LoanRepository;
import com.coeus.api.repositories.StudentRepository;
import com.coeus.api.repositories.security.UserRepository;
import com.coeus.api.models.mapper.LoanMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LoanControllerJsonTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;
    private static LoanResponseDTO loanResponseDTO;
    private static TokenDTO tokenDTO;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private LoanMapper loanMapper;

    @BeforeAll
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        if (userRepository.findByUsername("jpereira").isEmpty()) {
            var user = new User();
            user.setUsername("jpereira");
            user.setPassword(encoder.encode("123"));
            user.setRole(UserRole.COORDINATOR);
            user.setEmployeeId(2L);
            userRepository.save(user);
        }
    }

    @BeforeEach
    void resetBookStock() {
        bookRepository.findById(1L).ifPresent(book -> {
            book.setStock(10);
            bookRepository.save(book);
        });
    }

    @Order(0)
    @Test
    void login() {
        AuthenticationDTO credentials = new AuthenticationDTO("jpereira", "123");

        tokenDTO = given()
                .basePath("auth/login")
                .port(TestConfigs.SERVER_PORT)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(credentials)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(TokenDTO.class);

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.AUTHORIZED_ORIGIN)
                .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenDTO.getAccessToken())
                .setBasePath("/loans")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        assertNotNull(tokenDTO.getAccessToken());
        assertNotNull(tokenDTO.getRefreshToken());
    }

    @Test
    @Order(1)
    void createLoan() throws JsonProcessingException {
        LoanCreateDTO loanCreateDTO = new LoanCreateDTO();
        loanCreateDTO.setStudentId(1L);
        loanCreateDTO.setBookId(1L);

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(loanCreateDTO)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        loanResponseDTO = objectMapper.readValue(content, LoanResponseDTO.class);

        assertNotNull(loanResponseDTO);
        assertNotNull(loanResponseDTO.getId());
        assertEquals("ON_GOING", loanResponseDTO.getStatus().toString());
    }

    @Test
    @Order(2)
    void returnLoan() throws JsonProcessingException {
        assertNotNull(loanResponseDTO);

        var content = given(specification)
                .pathParam("id", loanResponseDTO.getId())
                .when()
                .patch("{id}/return")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        LoanResponseDTO returnedLoan = objectMapper.readValue(content, LoanResponseDTO.class);

        assertEquals("RETURNED", returnedLoan.getStatus().toString());
        assertNotNull(returnedLoan.getReturnDate());
    }

    @Test
    @Order(3)
    void findByStudent() throws JsonProcessingException {
        var content = given(specification)
                .pathParam("studentId", 1L)
                .queryParams("page", 0, "size", 12, "direction", "asc")
                .when()
                .get("/student/{studentId}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        WrapperLoanDTO wrapper = objectMapper.readValue(content, WrapperLoanDTO.class);

        assertNotNull(wrapper);
        assertNotNull(wrapper.getEmbedded());
        List<LoanResponseDTO> loans = wrapper.getEmbedded().getLoans();

        assertNotNull(loans);
        assertTrue(loans.size() >= 1);
    }

    @Test
    @Order(4)
    void findOverdue() throws JsonProcessingException {
        Loan overdueLoan = new Loan();
        overdueLoan.setStudent(studentRepository.findById(1L).orElseThrow());
        overdueLoan.setBook(bookRepository.findById(1L).orElseThrow());
        overdueLoan.setEmployee(employeeRepository.findById(1L).orElseThrow());
        overdueLoan.setLoanDate(LocalDate.now().minusDays(15));
        overdueLoan.setDueDate(LocalDate.now().minusDays(10));
        overdueLoan.setStatus(LoanStatus.ON_GOING);
        overdueLoan.setReturnDate(null);

        loanRepository.save(overdueLoan);

        var content = given(specification)
                .queryParams("page", 0, "size", 12, "direction", "asc")
                .when()
                .get("/overdue")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        WrapperLoanDTO wrapper = objectMapper.readValue(content, WrapperLoanDTO.class);

        assertNotNull(wrapper);
        assertNotNull(wrapper.getEmbedded());
        List<LoanResponseDTO> overdueLoans = wrapper.getEmbedded().getLoans();

        assertNotNull(overdueLoans);
        assertFalse(overdueLoans.isEmpty());

        overdueLoans.forEach(loanDTO ->
                assertTrue(loanDTO.getDueDate().isBefore(LocalDate.now()))
        );
    }
}
