package com.coeus.api.integrationtests.controllers.withjson;

import com.coeus.api.config.TestConfigs;
import com.coeus.api.integrationtests.dto.AuthenticationDTO;
import com.coeus.api.integrationtests.dto.TokenDTO;
import com.coeus.api.integrationtests.dto.WarningDTO;
import com.coeus.api.integrationtests.dto.wrapper.json.WrapperWarningDTO;
import com.coeus.api.integrationtests.testcontainers.AbstractIntegrationTest;
import com.coeus.api.models.security.user.User;
import com.coeus.api.models.security.user.UserRole;
import com.coeus.api.repositories.security.UserRepository;
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

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class WarningControllerJsonTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;
    private static WarningDTO warningDTO;
    private static TokenDTO tokenDTO;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @BeforeAll
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        if (userRepository.findByUsername("jpereira").isEmpty()) {
            User user = new User();
            user.setUsername("jpereira");
            user.setPassword(encoder.encode("123"));
            user.setRole(UserRole.COORDINATOR);
            user.setEmployeeId(2L);
            userRepository.save(user);
        }
    }

    @Test
    @Order(0)
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
                .setBasePath("/warnings")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        assertNotNull(tokenDTO.getAccessToken());
        assertNotNull(tokenDTO.getRefreshToken());
    }

    @Test
    @Order(1)
    void create() throws JsonProcessingException {
        warningDTO = new WarningDTO();
        warningDTO.setStudentId(1L);
        warningDTO.setEmployeeId(2L);
        warningDTO.setReason("Manual warning test");
        warningDTO.setDetails("Student returned the book in bad condition");

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(warningDTO)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        WarningDTO createdDTO = objectMapper.readValue(content, WarningDTO.class);
        warningDTO = createdDTO;

        assertNotNull(createdDTO.getId());
        assertEquals("Manual warning test", createdDTO.getReason());
        assertEquals("Student returned the book in bad condition", createdDTO.getDetails());
        assertEquals("PENDING", createdDTO.getStatus().toString());
    }

    @Test
    @Order(2)
    void resolve() throws JsonProcessingException {
        var content = given(specification)
                .pathParam("id", warningDTO.getId())
                .when()
                .put("{id}/resolve")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        WarningDTO resolvedDTO = objectMapper.readValue(content, WarningDTO.class);
        warningDTO = resolvedDTO;

        assertNotNull(resolvedDTO.getId());
        assertEquals("RESOLVED", resolvedDTO.getStatus().toString());
        assertNotNull(resolvedDTO.getResolvedAt());
        assertEquals(warningDTO.getId(), resolvedDTO.getId());
    }

    @Test
    @Order(3)
    void findById() throws JsonProcessingException {
        var content = given(specification)
                .pathParam("id", warningDTO.getId())
                .when()
                .get("{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        WarningDTO foundDTO = objectMapper.readValue(content, WarningDTO.class);

        assertNotNull(foundDTO);
        assertEquals(warningDTO.getId(), foundDTO.getId());
        assertEquals(warningDTO.getStudentId(), foundDTO.getStudentId());
        assertEquals(warningDTO.getEmployeeId(), foundDTO.getEmployeeId());
        assertEquals(warningDTO.getReason(), foundDTO.getReason());
        assertEquals(warningDTO.getDetails(), foundDTO.getDetails());
        assertEquals(warningDTO.getStatus(), foundDTO.getStatus());
    }

    @Test
    @Order(4)
    void findAll() throws JsonProcessingException {
        var content = given(specification)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParams("page", 0, "size", 12, "direction", "asc")
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        WrapperWarningDTO wrapper = objectMapper.readValue(content, WrapperWarningDTO.class);
        List<WarningDTO> warnings = wrapper.getEmbedded().getWarnings();

        WarningDTO warningOne = warnings.get(0);

        assertNotNull(warningOne.getId());
        assertTrue(warningOne.getId() > 0);

        assertEquals("Manual warning test", warningOne.getReason());
        assertEquals("Student returned the book in bad condition", warningOne.getDetails());
        assertEquals(1L, warningOne.getStudentId());
        assertEquals(2L, warningOne.getEmployeeId());
    }

    @Test
    @Order(5)
    void findByStudent() throws JsonProcessingException {
        var content = given(specification)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("studentId", 1L)
                .queryParams("page", 0, "size", 12, "direction", "asc")
                .when()
                .get("/student/{studentId}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        WrapperWarningDTO wrapper = objectMapper.readValue(content, WrapperWarningDTO.class);
        List<WarningDTO> warnings = wrapper.getEmbedded().getWarnings();

        WarningDTO warningOne = warnings.get(0);

        assertNotNull(warningOne.getId());
        assertTrue(warningOne.getId() > 0);

        assertEquals("Manual warning test", warningOne.getReason());
        assertEquals("Student returned the book in bad condition", warningOne.getDetails());
        assertEquals(1L, warningOne.getStudentId());
        assertEquals(2L, warningOne.getEmployeeId());
    }

    @Test
    @Order(6)
    void delete() {
        assertNotNull(warningDTO);
        assertTrue(warningDTO.getId() > 0);

        given(specification)
                .pathParam("id", warningDTO.getId())
                .when()
                .delete("{id}")
                .then()
                .statusCode(204);
    }

}