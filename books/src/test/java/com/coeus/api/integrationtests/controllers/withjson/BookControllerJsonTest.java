package com.coeus.api.integrationtests.controllers.withjson;

import com.coeus.api.config.TestConfigs;
import com.coeus.api.integrationtests.dto.AuthenticationDTO;
import com.coeus.api.integrationtests.dto.TokenDTO;
import com.coeus.api.integrationtests.dto.wrapper.json.WrapperBookDTO;
import com.coeus.api.integrationtests.testcontainers.AbstractIntegrationTest;
import com.coeus.api.integrationtests.dto.BookDTO;

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
class BookControllerJsonTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;
    private static BookDTO bookDTO;

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

        bookDTO = new BookDTO();

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
                .setBasePath("/books")
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
        mockBook();

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(bookDTO)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        // rest-assured uses its own object mapper for serialization, which may cause problems.
        // to avoid that, we are deserializing the response manually.
        BookDTO createdDTO = objectMapper.readValue(content, BookDTO.class);
        bookDTO = createdDTO;
// student
        assertNotNull(createdDTO.getId());
        assertTrue(createdDTO.getId() > 0);

        assertEquals("MockBook", createdDTO.getBookName());
        assertEquals("MockAuthor", createdDTO.getAuthorName());
        assertEquals("MockPublisher", createdDTO.getPublisherName());
        assertEquals(11, createdDTO.getNumberOfPages());
        assertEquals("MockGenre", createdDTO.getGenre());
        assertEquals(11, createdDTO.getPrice());
    }

    @Test
    @Order(2)
    void update() throws JsonProcessingException {
        bookDTO.setBookName("MockBookName");

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(bookDTO)
                .when()
                .put()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        BookDTO createdDTO = objectMapper.readValue(content, BookDTO.class);
        bookDTO = createdDTO;

        assertNotNull(createdDTO.getId());

        assertTrue(createdDTO.getId() > 0);

        assertEquals("MockBookName", createdDTO.getBookName());
        assertEquals("MockAuthor", createdDTO.getAuthorName());
        assertEquals("MockPublisher", createdDTO.getPublisherName());
        assertEquals(11, createdDTO.getNumberOfPages());
        assertEquals("MockGenre", createdDTO.getGenre());
        assertEquals(11, createdDTO.getPrice());
    }

    @Test
    @Order(3)
    void findById() throws JsonProcessingException {

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("id", bookDTO.getId())
                .when()
                .get("{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        BookDTO createdDTO = objectMapper.readValue(content, BookDTO.class);
        bookDTO = createdDTO;

        assertNotNull(createdDTO.getId());
        assertTrue(createdDTO.getId() > 0);

        assertEquals("MockBookName", createdDTO.getBookName());
        assertEquals("MockAuthor", createdDTO.getAuthorName());
        assertEquals("MockPublisher", createdDTO.getPublisherName());
        assertEquals(11, createdDTO.getNumberOfPages());
        assertEquals("MockGenre", createdDTO.getGenre());
        assertEquals(11, createdDTO.getPrice());
    }

    @Test
    @Order(4)
    void delete() throws JsonProcessingException {

        given(specification)
                .pathParam("id", bookDTO.getId())
                .when()
                .delete("{id}")
                .then()
                .statusCode(204);
    }

    @Test
    @Order(5)
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

        WrapperBookDTO wrapper = objectMapper.readValue(content, WrapperBookDTO.class);
        List<BookDTO> books = wrapper.getEmbedded().getBooks();

        BookDTO bookOne = books.get(0);

        assertNotNull(bookOne.getId());
        assertTrue(bookOne.getId() > 0);

        assertEquals("The Lady with the Dog", bookOne.getBookName());
        assertEquals("Anton Chekhov", bookOne.getAuthorName());
        assertEquals("Penguin Random House", bookOne.getPublisherName());
        assertEquals(285, bookOne.getNumberOfPages());
        assertEquals("Non-fiction", bookOne.getGenre());
        assertEquals(59, bookOne.getPrice());

        BookDTO bookTwo = books.get(2);

        assertNotNull(bookTwo.getId());
        assertTrue(bookTwo.getId() > 0);

        assertEquals("The Man in a Case", bookTwo.getBookName());
        assertEquals("Anton Chekhov", bookTwo.getAuthorName());
        assertEquals("Algonquin Books", bookTwo.getPublisherName());
        assertEquals(331, bookTwo.getNumberOfPages());
        assertEquals("Science Fiction", bookTwo.getGenre());
        assertEquals(32, bookTwo.getPrice());
    }

    private void mockBook() {
        bookDTO.setBookName("MockBook");
        bookDTO.setAuthorName("MockAuthor");
        bookDTO.setPublisherName("MockPublisher");
        bookDTO.setNumberOfPages(11);
        bookDTO.setGenre("MockGenre");
        bookDTO.setPrice(11);
    }

}