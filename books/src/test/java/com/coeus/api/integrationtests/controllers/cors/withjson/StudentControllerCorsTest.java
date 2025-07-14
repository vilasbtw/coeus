package com.coeus.api.integrationtests.controllers.cors.withjson;

import com.coeus.api.config.TestConfigs;
import com.coeus.api.integrationtests.dto.AuthenticationDTO;
import com.coeus.api.integrationtests.dto.StudentDTO;
import com.coeus.api.integrationtests.dto.TokenDTO;
import com.coeus.api.integrationtests.testcontainers.AbstractIntegrationTest;
import com.coeus.api.models.security.user.User;
import com.coeus.api.models.security.user.UserRole;
import com.coeus.api.repositories.security.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static io.restassured.RestAssured.given;
import static junit.framework.TestCase.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class StudentControllerCorsTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;
    private static StudentDTO studentDTO;
    private static TokenDTO tokenDTO;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @BeforeAll
    void setUp() {
        objectMapper = new ObjectMapper();
        // ignores HATEOAS links.
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        studentDTO = new StudentDTO();

        if (userRepository.findByUsername("kvilas").isEmpty()) {
            var user = new User();

            user.setUsername("kvilas");
            user.setPassword(encoder.encode("123"));
            user.setRole(UserRole.COORDINATOR);

            userRepository.save(user);
        }
    }

    @Order(1)
    @Test
    void login() {
        AuthenticationDTO credentials = new AuthenticationDTO("kvilas", "123");

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

        assertNotNull(tokenDTO.getAccessToken());
        assertNotNull(tokenDTO.getRefreshToken());
    }

    @Test
    @Order(2)
    void create() throws JsonProcessingException {
        mockStudent();

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.AUTHORIZED_ORIGIN)
                .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenDTO.getAccessToken())
                .setBasePath("/students")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(studentDTO)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        // rest-assured uses its own object mapper for serialization, which may cause problems.
        // to avoid that, we are deserializing the response manually.
        StudentDTO createdDTO = objectMapper.readValue(content, StudentDTO.class);
        studentDTO = createdDTO;

        assertNotNull(createdDTO.getId());
        assertNotNull(createdDTO.getStudentRegister());
        assertNotNull(createdDTO.getName());
        assertNotNull(createdDTO.getEmail());
        assertNotNull(createdDTO.getCourse());

        assertTrue(createdDTO.getId() > 0);

        assertEquals("HT3036901", createdDTO.getStudentRegister());
        assertEquals("Kaique", createdDTO.getName());
        assertEquals("kaique@gmail.com", createdDTO.getEmail());
        assertEquals("System Development And Analysis", createdDTO.getCourse());
        assertTrue(studentDTO.getEnabled());
    }

    @Test
    @Order(3)
    void createWithWrongOrigin() throws JsonProcessingException {
        mockStudent();

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.UNATHOURIZED_ORIGIN)
                .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenDTO.getAccessToken())
                .setBasePath("/students")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(studentDTO)
                .when()
                .post()
                .then()
                .statusCode(403)
                .extract()
                .body()
                .asString();

        assertEquals("Invalid CORS request", content);
    }

    @Test
    @Order(4)
    void findById() throws JsonProcessingException {
        mockStudent();

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.LOCALHOST_ORIGIN)
                .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenDTO.getAccessToken())
                .setBasePath("/students")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("id", studentDTO.getId())
                .when()
                .get("{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        // rest-assured uses its own object mapper for serialization, which may cause problems.
        // to avoid that, we are deserializing the response manually.
        StudentDTO createdDTO = objectMapper.readValue(content, StudentDTO.class);
        studentDTO = createdDTO;

        assertNotNull(createdDTO.getId());
        assertNotNull(createdDTO.getStudentRegister());
        assertNotNull(createdDTO.getName());
        assertNotNull(createdDTO.getEmail());
        assertNotNull(createdDTO.getCourse());

        assertTrue(createdDTO.getId() > 0);

        assertEquals("HT3036901", createdDTO.getStudentRegister());
        assertEquals("Kaique", createdDTO.getName());
        assertEquals("kaique@gmail.com", createdDTO.getEmail());
        assertEquals("System Development And Analysis", createdDTO.getCourse());
        assertTrue(studentDTO.getEnabled());
    }

    @Test
    @Order(5)
    void findByIdWithWrongOrigin() throws JsonProcessingException {
        mockStudent();

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.UNATHOURIZED_ORIGIN)
                .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenDTO.getAccessToken())
                .setBasePath("/students")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("id", studentDTO.getId())
                .when()
                .get("{id}")
                .then()
                .statusCode(403)
                .extract()
                .body()
                .asString();

        assertEquals("Invalid CORS request", content);

    }

    private void mockStudent() {
        studentDTO.setStudentRegister("HT3036901");
        studentDTO.setName("Kaique");
        studentDTO.setEmail("kaique@gmail.com");
        studentDTO.setCourse("System Development And Analysis");
    }

}