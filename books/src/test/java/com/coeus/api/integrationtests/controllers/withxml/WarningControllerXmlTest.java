package com.coeus.api.integrationtests.controllers.withxml;

import com.coeus.api.config.TestConfigs;
import com.coeus.api.integrationtests.dto.AuthenticationDTO;
import com.coeus.api.integrationtests.dto.TokenDTO;
import com.coeus.api.integrationtests.dto.WarningDTO;
import com.coeus.api.integrationtests.dto.wrapper.xml.PagedModelWarning;
import com.coeus.api.integrationtests.testcontainers.AbstractIntegrationTest;
import com.coeus.api.models.security.user.User;
import com.coeus.api.models.security.user.UserRole;
import com.coeus.api.repositories.security.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
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

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class WarningControllerXmlTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static XmlMapper objectMapper;
    private static WarningDTO warningDTO;
    private static TokenDTO tokenDTO;

    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder encoder;

    @BeforeAll
    void setUp() {
        objectMapper = new XmlMapper();
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

    @Test
    @Order(0)
    void login() throws JsonProcessingException {
        AuthenticationDTO credentials = new AuthenticationDTO("jpereira", "123");

        var content = given()
                .basePath("auth/login")
                .port(TestConfigs.SERVER_PORT)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .accept(MediaType.APPLICATION_XML_VALUE)
                .body(credentials)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        tokenDTO = objectMapper.readValue(content, TokenDTO.class);

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
        warningDTO.setDetails("Book damaged");

        var content = given(specification)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .accept(MediaType.APPLICATION_XML_VALUE)
                .body(warningDTO)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        warningDTO = objectMapper.readValue(content, WarningDTO.class);

        assertNotNull(warningDTO.getId());
        assertEquals("Manual warning test", warningDTO.getReason());
        assertEquals("Book damaged", warningDTO.getDetails());
        assertEquals("PENDING", warningDTO.getStatus().toString());
    }

    @Test
    @Order(2)
    void resolve() throws JsonProcessingException {
        var content = given(specification)
                .accept(MediaType.APPLICATION_XML_VALUE)
                .pathParam("id", warningDTO.getId())
                .when()
                .put("{id}/resolve")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        warningDTO = objectMapper.readValue(content, WarningDTO.class);

        assertEquals("RESOLVED", warningDTO.getStatus().toString());
        assertNotNull(warningDTO.getResolvedAt());
    }

    @Test
    @Order(3)
    void findById() throws JsonProcessingException {
        var content = given(specification)
                .accept(MediaType.APPLICATION_XML_VALUE)
                .pathParam("id", warningDTO.getId())
                .when()
                .get("{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        WarningDTO found = objectMapper.readValue(content, WarningDTO.class);

        assertEquals(warningDTO.getId(), found.getId());
        assertEquals(warningDTO.getStudentId(), found.getStudentId());
        assertEquals(warningDTO.getEmployeeId(), found.getEmployeeId());
        assertEquals(warningDTO.getReason(), found.getReason());
        assertEquals(warningDTO.getDetails(), found.getDetails());
        assertEquals(warningDTO.getStatus(), found.getStatus());
    }

    @Test
    @Order(4)
    void findAll() throws JsonProcessingException {
        var content = given(specification)
                .accept(MediaType.APPLICATION_XML_VALUE)
                .queryParams("page", 0, "size", 12, "direction", "asc")
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        PagedModelWarning wrapper = objectMapper.readValue(content, PagedModelWarning.class);

        assertNotNull(wrapper);
        assertNotNull(wrapper.getContent());
        assertFalse(wrapper.getContent().isEmpty());
    }

    @Test
    @Order(5)
    void findByStudent() throws JsonProcessingException {
        var content = given(specification)
                .accept(MediaType.APPLICATION_XML_VALUE)
                .pathParam("studentId", 1L)
                .queryParams("page", 0, "size", 12, "direction", "asc")
                .when()
                .get("/student/{studentId}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        PagedModelWarning wrapper = objectMapper.readValue(content, PagedModelWarning.class);

        assertNotNull(wrapper);
        assertNotNull(wrapper.getContent());
        assertFalse(wrapper.getContent().isEmpty());
    }

    @Test
    @Order(6)
    void delete() {
        given(specification)
                .pathParam("id", warningDTO.getId())
                .when()
                .delete("{id}")
                .then()
                .statusCode(204);
    }
}
