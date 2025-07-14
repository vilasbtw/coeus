package com.coeus.api.integrationtests.controllers.withjson;

import com.coeus.api.config.TestConfigs;
import com.coeus.api.integrationtests.dto.AuthenticationDTO;
import com.coeus.api.integrationtests.dto.StudentDTO;
import com.coeus.api.integrationtests.dto.TokenDTO;
import com.coeus.api.integrationtests.dto.wrapper.json.WrapperStudentDTO;
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

import java.util.List;

import static io.restassured.RestAssured.given;
import static junit.framework.TestCase.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class StudentControllerJsonTest extends AbstractIntegrationTest {

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

    @Order(0)
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

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.AUTHORIZED_ORIGIN)
                .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenDTO.getAccessToken())
                .setBasePath("/students")
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
        mockStudent();

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
                .queryParams("page", 3, "size", 12, "direction", "asc")
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        WrapperStudentDTO wrapper = objectMapper.readValue(content, WrapperStudentDTO.class);
        List<StudentDTO> students = wrapper.getEmbedded().getStudents();

        StudentDTO StudentOne = students.get(0);

        assertNotNull(StudentOne.getId());
        assertTrue(StudentOne.getId() > 0);

        assertEquals("306809005", StudentOne.getStudentRegister());
        assertEquals("Amble", StudentOne.getName());
        assertEquals("adanilov4q@gizmodo.com", StudentOne.getEmail());
        assertEquals("Biology", StudentOne.getCourse());
        assertTrue(StudentOne.getEnabled());

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

        // {{baseUrl}}/students/findByName/and?page=0&size=12&direction=asc

        var content = given(specification)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("name", "and")
                .queryParams("page", 0, "size", 12, "direction", "asc")
                .when()
                .get("findByName/{name}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        WrapperStudentDTO wrapper = objectMapper.readValue(content, WrapperStudentDTO.class);
        List<StudentDTO> students = wrapper.getEmbedded().getStudents();

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