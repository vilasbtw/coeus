package com.coeus.api.integrationtests.controllers.withyaml;

import com.coeus.api.config.TestConfigs;
import com.coeus.api.integrationtests.controllers.withyaml.mapper.YAMLMapper;
import com.coeus.api.integrationtests.dto.BookDTO;
import com.coeus.api.integrationtests.dto.wrapper.xml.PagedModelBook;
import com.coeus.api.integrationtests.testcontainers.AbstractIntegrationTest;
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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.util.List;

import static io.restassured.RestAssured.given;
import static junit.framework.TestCase.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BookControllerYamlTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static YAMLMapper objectMapper;
    private static BookDTO bookDTO;

    @BeforeAll
    static void setUp() {
        objectMapper = new YAMLMapper();
        bookDTO = new BookDTO();
    }

    @Test
    @Order(1)
    void create() throws JsonProcessingException {
        mockBook();

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.AUTHORIZED_ORIGIN)
                .setBasePath("/books")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var createdDTO = given().config(
                        RestAssuredConfig.config().encoderConfig(
                                EncoderConfig.encoderConfig().
                                        encodeContentTypeAs(MediaType.APPLICATION_YAML_VALUE, ContentType.TEXT))
                )
                .spec(specification)
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                .accept(MediaType.APPLICATION_YAML_VALUE)
                .body(bookDTO, objectMapper)
                .when()
                .post()
                .then()
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                .statusCode(200)
                .extract()
                .body()
                .as(BookDTO.class, objectMapper);

        bookDTO = createdDTO;

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
        bookDTO.setBookName("NewMockBookName");

        var updatedDTO = given().config(
                        RestAssuredConfig.config().encoderConfig(
                                EncoderConfig.encoderConfig().
                                        encodeContentTypeAs(MediaType.APPLICATION_YAML_VALUE, ContentType.TEXT))
                )
                .spec(specification)
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                .accept(MediaType.APPLICATION_YAML_VALUE)
                .body(bookDTO, objectMapper)
                .when()
                .put()
                .then()
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                .statusCode(200)
                .extract()
                .body()
                .as(BookDTO.class, objectMapper);

        bookDTO = updatedDTO;

        assertNotNull(updatedDTO.getId());
        assertTrue(updatedDTO.getId() > 0);

        assertEquals("NewMockBookName", updatedDTO.getBookName());
        assertEquals("MockAuthor", updatedDTO.getAuthorName());
        assertEquals("MockPublisher", updatedDTO.getPublisherName());
        assertEquals(11, updatedDTO.getNumberOfPages());
        assertEquals("MockGenre", updatedDTO.getGenre());
        assertEquals(11, updatedDTO.getPrice());
    }

    @Test
    @Order(3)
    void findById() throws JsonProcessingException {

        var foundDTO = given().config(
                        RestAssuredConfig.config().encoderConfig(
                                EncoderConfig.encoderConfig().
                                        encodeContentTypeAs(MediaType.APPLICATION_YAML_VALUE, ContentType.TEXT))
                )
                .spec(specification)
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                .accept(MediaType.APPLICATION_YAML_VALUE)
                .pathParam("id", bookDTO.getId())
                .when()
                .get("{id}")
                .then()
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                .statusCode(200)
                .extract()
                .body()
                .as(BookDTO.class, objectMapper);

        bookDTO = foundDTO;

        assertNotNull(foundDTO.getId());
        assertTrue(foundDTO.getId() > 0);

        assertEquals("NewMockBookName", foundDTO.getBookName());
        assertEquals("MockAuthor", foundDTO.getAuthorName());
        assertEquals("MockPublisher", foundDTO.getPublisherName());
        assertEquals(11, foundDTO.getNumberOfPages());
        assertEquals("MockGenre", foundDTO.getGenre());
        assertEquals(11, foundDTO.getPrice());
    }

    @Test
    @Order(4)
    void delete() {
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

        var response = given().config(
                        RestAssuredConfig.config().encoderConfig(
                                EncoderConfig.encoderConfig().
                                        encodeContentTypeAs(MediaType.APPLICATION_YAML_VALUE, ContentType.TEXT))
                )
                .spec(specification)
                .accept(MediaType.APPLICATION_YAML_VALUE)
                .queryParams("page", 3, "size", 12, "direction", "asc")
                .when()
                .get()
                .then()
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                .statusCode(200)
                .extract()
                .body()
                .as(PagedModelBook.class, objectMapper);

        List<BookDTO> books = response.getContent();

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
