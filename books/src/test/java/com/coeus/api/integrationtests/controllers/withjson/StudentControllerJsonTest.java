package com.coeus.api.integrationtests.controllers.withjson;

import com.coeus.api.config.TestConfigs;
import com.coeus.api.integrationtests.dto.StudentDTO;
import com.coeus.api.integrationtests.testcontainers.AbstractIntegrationTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;
import static junit.framework.TestCase.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class StudentControllerJsonTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;
    private static StudentDTO studentDTO;

    @BeforeAll
    static void setUp() {
        objectMapper = new ObjectMapper();
        // ignores HATEOAS links.
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        studentDTO = new StudentDTO();
    }

    @Test
    @Order(1)
    void create() throws JsonProcessingException {
        mockStudent();

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.AUTHORIZED_ORIGIN)
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

        assertTrue(createdDTO.getId() > 0);

        assertEquals("HT3035502", createdDTO.getStudentRegister());
        assertEquals("Linus", createdDTO.getName());
        assertEquals("Linus@gmail.com", createdDTO.getEmail());
        assertEquals("Computer Science", createdDTO.getCourse());
        assertTrue(studentDTO.getEnabled());
    }

    @Test
    @Order(2)
    void update() throws JsonProcessingException {
        studentDTO.setName("Linus Benedict Torvalds");

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(studentDTO)
                .when()
                .put()
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

        assertTrue(createdDTO.getId() > 0);

        assertEquals("HT3035502", createdDTO.getStudentRegister());
        assertEquals("Linus Benedict Torvalds", createdDTO.getName());
        assertEquals("Linus@gmail.com", createdDTO.getEmail());
        assertEquals("Computer Science", createdDTO.getCourse());
        assertTrue(studentDTO.getEnabled());
    }

    @Test
    @Order(3)
    void findById() throws JsonProcessingException {
        mockStudent();

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
        assertTrue(createdDTO.getId() > 0);

        assertEquals("HT3035502", createdDTO.getStudentRegister());
        assertEquals("Linus Benedict Torvalds", createdDTO.getName());
        assertEquals("Linus@gmail.com", createdDTO.getEmail());
        assertEquals("Computer Science", createdDTO.getCourse());
        assertTrue(studentDTO.getEnabled());
    }

    private void mockStudent() {
        studentDTO.setStudentRegister("HT3035502");
        studentDTO.setName("Linus");
        studentDTO.setEmail("Linus@gmail.com");
        studentDTO.setCourse("Computer Science");
    }

}