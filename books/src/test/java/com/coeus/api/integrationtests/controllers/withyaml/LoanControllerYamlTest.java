package com.coeus.api.integrationtests.controllers.withyaml;

import com.coeus.api.config.TestConfigs;
import com.coeus.api.integrationtests.controllers.withyaml.mapper.YAMLMapper;
import com.coeus.api.integrationtests.dto.AuthenticationDTO;
import com.coeus.api.integrationtests.dto.LoanCreateDTO;
import com.coeus.api.integrationtests.dto.LoanResponseDTO;
import com.coeus.api.integrationtests.dto.TokenDTO;
import com.coeus.api.integrationtests.dto.wrapper.xml.PagedModelLoan;
import com.coeus.api.integrationtests.testcontainers.AbstractIntegrationTest;
import com.coeus.api.models.security.user.User;
import com.coeus.api.models.security.user.UserRole;
import com.coeus.api.repositories.BookRepository;
import com.coeus.api.repositories.LoanRepository;
import com.coeus.api.repositories.StudentRepository;
import com.coeus.api.repositories.EmployeeRepository;
import com.coeus.api.repositories.security.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LoanControllerYamlTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static YAMLMapper objectMapper;
    private static LoanResponseDTO loanResponseDTO;
    private static TokenDTO tokenDTO;

    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder encoder;
    @Autowired private BookRepository bookRepository;
    @Autowired private StudentRepository studentRepository;
    @Autowired private EmployeeRepository employeeRepository;
    @Autowired private LoanRepository loanRepository;

    @BeforeAll
    void setUp() {
        objectMapper = new YAMLMapper();

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

    @Test
    @Order(0)
    void login() throws JsonProcessingException {
        AuthenticationDTO credentials = new AuthenticationDTO("jpereira", "123");

        tokenDTO = given().config(
                    RestAssuredConfig.config().encoderConfig(
                        EncoderConfig.encoderConfig()
                            .encodeContentTypeAs(MediaType.APPLICATION_YAML_VALUE, ContentType.TEXT)))
                .basePath("auth/login")
                .port(TestConfigs.SERVER_PORT)
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                .accept(MediaType.APPLICATION_YAML_VALUE)
                .body(credentials, objectMapper)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(TokenDTO.class, objectMapper);

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

        loanResponseDTO = given().config(
                        RestAssuredConfig.config().encoderConfig(
                                EncoderConfig.encoderConfig().
                                        encodeContentTypeAs(MediaType.APPLICATION_YAML_VALUE, ContentType.TEXT))
                )
                .spec(specification)
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                .accept(MediaType.APPLICATION_YAML_VALUE)
                .body(loanCreateDTO, objectMapper)
                .when()
                .post()
                .then()
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                .statusCode(200)
                .extract()
                .body()
                .as(LoanResponseDTO.class, objectMapper);

        assertNotNull(loanResponseDTO);
        assertNotNull(loanResponseDTO.getId());
        assertEquals("ON_GOING", loanResponseDTO.getStatus().toString());
    }

    @Test
    @Order(2)
    void returnLoan() throws JsonProcessingException {
        var returnedLoan = given().config(
                        RestAssuredConfig.config().encoderConfig(
                                EncoderConfig.encoderConfig().
                                        encodeContentTypeAs(MediaType.APPLICATION_YAML_VALUE, ContentType.TEXT))
                )
                .spec(specification)
                .accept(MediaType.APPLICATION_YAML_VALUE)
                .pathParam("id", loanResponseDTO.getId())
                .when()
                .patch("{id}/return")
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                .extract()
                .body()
                .as(LoanResponseDTO.class, objectMapper);

        assertEquals("RETURNED", returnedLoan.getStatus().toString());
        assertNotNull(returnedLoan.getReturnDate());
    }

    @Test
    @Order(3)
    void findByStudent() throws JsonProcessingException {
        var response = given().config(
                        RestAssuredConfig.config().encoderConfig(
                                EncoderConfig.encoderConfig().
                                        encodeContentTypeAs(MediaType.APPLICATION_YAML_VALUE, ContentType.TEXT))
                )
                .spec(specification)
                .accept(MediaType.APPLICATION_YAML_VALUE)
                .pathParam("studentId", 1L)
                .queryParams("page", 0, "size", 12, "direction", "asc")
                .when()
                .get("/student/{studentId}")
                .then()
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                .statusCode(200)
                .extract()
                .body()
                .as(PagedModelLoan.class, objectMapper);

        List<LoanResponseDTO> loans = response.getContent();

        assertNotNull(loans);
        assertFalse(loans.isEmpty());

        LoanResponseDTO loan = loans.getFirst();

        assertNotNull(loan.getId());
        assertEquals("Kaique Vilas Boa", loan.getStudentName());
        assertEquals("1984", loan.getBookName());
        assertEquals("João Pereira", loan.getEmployeeName());
    }

    @Test
    @Order(4)
    void findOverdue() throws JsonProcessingException {
        var response = given().config(
                        RestAssuredConfig.config().encoderConfig(
                                EncoderConfig.encoderConfig().
                                        encodeContentTypeAs(MediaType.APPLICATION_YAML_VALUE, ContentType.TEXT))
                )
                .spec(specification)
                .accept(MediaType.APPLICATION_YAML_VALUE)
                .queryParams("page", 0, "size", 12, "direction", "asc")
                .when()
                .get("/overdue")
                .then()
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                .statusCode(200)
                .extract()
                .body()
                .as(PagedModelLoan.class, objectMapper);

        List<LoanResponseDTO> loans = response.getContent();

        assertNotNull(loans);
        assertTrue(loans.isEmpty());
    }
}
