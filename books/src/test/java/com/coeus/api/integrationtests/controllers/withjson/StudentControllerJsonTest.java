package com.coeus.api.integrationtests.controllers.withjson;

import com.coeus.api.config.TestConfigs;
import com.coeus.api.integrationtests.dto.StudentDTO;
import com.coeus.api.integrationtests.testcontainers.AbstractIntegrationTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
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

import java.util.List;

import static io.restassured.RestAssured.given;
import static junit.framework.TestCase.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

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
        // mockStudent();

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

    @Test
    @Order(4)
    void disable() throws JsonProcessingException {
        mockStudent();

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("id", studentDTO.getId())
                .when()
                .patch("{id}")
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
        assertFalse(studentDTO.getEnabled());
    }

    @Test
    @Order(5)
    void delete() throws JsonProcessingException {

        given(specification)
                .pathParam("id", studentDTO.getId())
                .when()
                .delete("{id}")
                .then()
                .statusCode(204);
    }

    @Test
    @Order(6)
    void findAll() throws JsonProcessingException {

        var content = given(specification)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        List<StudentDTO> students = objectMapper.readValue(content, new TypeReference<List<StudentDTO>>() {});

        // rest-assured uses its own object mapper for serialization, which may cause problems.
        // to avoid that, we are deserializing the response manually.
        StudentDTO StudentOne = students.get(0);

        assertNotNull(StudentOne.getId());
        assertTrue(StudentOne.getId() > 0);

        assertEquals("2023001", StudentOne.getStudentRegister());
        assertEquals("Alice Martins Silva", StudentOne.getName());
        assertEquals("alice.silva@example.com", StudentOne.getEmail());
        assertEquals("Computer Science", StudentOne.getCourse());
        assertTrue(StudentOne.getEnabled());

        StudentDTO studentTwo = students.get(2);

        assertNotNull(studentTwo.getId());
        assertTrue(studentTwo.getId() > 0);

        assertEquals("2023003", studentTwo.getStudentRegister());
        assertEquals("Carla Souza Andrade", studentTwo.getName());
        assertEquals("carla.andrade@example.com", studentTwo.getEmail());
        assertEquals("Business Administration", studentTwo.getCourse());
        assertTrue(studentTwo.getEnabled());
    }

    private void mockStudent() {
        studentDTO.setStudentRegister("HT3035502");
        studentDTO.setName("Linus");
        studentDTO.setEmail("Linus@gmail.com");
        studentDTO.setCourse("Computer Science");
    }

}