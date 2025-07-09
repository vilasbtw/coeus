package com.coeus.api.integrationtests.controllers.withxml;

import com.coeus.api.config.TestConfigs;
import com.coeus.api.integrationtests.dto.StudentDTO;
import com.coeus.api.integrationtests.dto.wrapper.json.WrapperStudentDTO;
import com.coeus.api.integrationtests.dto.wrapper.xml.PagedModelStudent;
import com.coeus.api.integrationtests.testcontainers.AbstractIntegrationTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
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
class StudentControllerXmlTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static XmlMapper objectMapper;
    private static StudentDTO studentDTO;

    @BeforeAll
    static void setUp() {
        objectMapper = new XmlMapper();
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
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .accept(MediaType.APPLICATION_XML_VALUE)
                .body(studentDTO)
                .when()
                .post()
                .then()
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .statusCode(200)
                .extract()
                .body()
                .asString();

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
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .accept(MediaType.APPLICATION_XML_VALUE)
                .body(studentDTO)
                .when()
                .put()
                .then()
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .statusCode(200)
                .extract()
                .body()
                .asString();

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
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .accept(MediaType.APPLICATION_XML_VALUE)
                .pathParam("id", studentDTO.getId())
                .when()
                .get("{id}")
                .then()
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .statusCode(200)
                .extract()
                .body()
                .asString();

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
                .accept(MediaType.APPLICATION_XML_VALUE)
                .pathParam("id", studentDTO.getId())
                .when()
                .patch("{id}")
                .then()
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .statusCode(200)
                .extract()
                .body()
                .asString();

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
                .accept(MediaType.APPLICATION_XML_VALUE)
                .queryParams("page", 3, "size", 12, "direction", "asc")
                .when()
                .get()
                .then()
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .statusCode(200)
                .extract()
                .body()
                .asString();

        PagedModelStudent wrapper = objectMapper.readValue(content, PagedModelStudent.class);
        List<StudentDTO> students = wrapper.getContent();

        StudentDTO studentOne = students.get(0);

        assertNotNull(studentOne.getId());
        assertTrue(studentOne.getId() > 0);

        assertEquals("306809005", studentOne.getStudentRegister());
        assertEquals("Amble", studentOne.getName());
        assertEquals("adanilov4q@gizmodo.com", studentOne.getEmail());
        assertEquals("Biology", studentOne.getCourse());
        assertTrue(studentOne.getEnabled());

        StudentDTO studentTwo = students.get(2);

        assertNotNull(studentTwo.getId());
        assertTrue(studentTwo.getId() > 0);

        assertEquals("941229837", studentTwo.getStudentRegister());
        assertEquals("Ambrose", studentTwo.getName());
        assertEquals("acanto3y@example.com", studentTwo.getEmail());
        assertEquals("Medicine", studentTwo.getCourse());
        assertFalse(studentTwo.getEnabled());
    }

    @Test
    @Order(7)
    void findByName() throws JsonProcessingException {

        var content = given(specification)
                .accept(MediaType.APPLICATION_XML_VALUE)
                .pathParam("name", "and")
                .queryParams("page", 0, "size", 12, "direction", "asc")
                .when()
                .get("findByName/{name}")
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .extract()
                .body()
                .asString();

        PagedModelStudent wrapper = objectMapper.readValue(content, PagedModelStudent.class);
        List<StudentDTO> students = wrapper.getContent();

        StudentDTO StudentOne = students.get(0);

        assertNotNull(StudentOne.getId());
        assertTrue(StudentOne.getId() > 0);

        assertEquals("093855501", StudentOne.getStudentRegister());
        assertEquals("Aleksandr", StudentOne.getName());
        assertEquals("amouserkt@fastcompany.com", StudentOne.getEmail());
        assertEquals("Medicine", StudentOne.getCourse());
        assertTrue(StudentOne.getEnabled());

        StudentDTO studentTwo = students.get(2);

        assertNotNull(studentTwo.getId());
        assertTrue(studentTwo.getId() > 0);

        assertEquals("HT3012209", studentTwo.getStudentRegister());
        assertEquals("Alexandre Magno Abrão", studentTwo.getName());
        assertEquals("chorao013@gmail.com", studentTwo.getEmail());
        assertEquals("Economics", studentTwo.getCourse());
        assertTrue(studentTwo.getEnabled());
    }

    private void mockStudent() {
        studentDTO.setStudentRegister("HT3035502");
        studentDTO.setName("Linus");
        studentDTO.setEmail("Linus@gmail.com");
        studentDTO.setCourse("Computer Science");
    }

}