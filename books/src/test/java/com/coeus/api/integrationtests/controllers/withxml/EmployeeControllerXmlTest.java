package com.coeus.api.integrationtests.controllers.withxml;

import com.coeus.api.config.TestConfigs;
import com.coeus.api.integrationtests.dto.EmployeeDTO;
import com.coeus.api.integrationtests.dto.wrapper.xml.PagedModelEmployee;
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
class EmployeeControllerXmlTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static XmlMapper objectMapper;
    private static EmployeeDTO employeeDTO;

    @BeforeAll
    static void setUp() {
        objectMapper = new XmlMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        employeeDTO = new EmployeeDTO();
    }

    @Test
    @Order(1)
    void create() throws JsonProcessingException {
        mockEmployee();

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.AUTHORIZED_ORIGIN)
                .setBasePath("/employees")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = given(specification)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .accept(MediaType.APPLICATION_XML_VALUE)
                .body(employeeDTO)
                .when()
                .post()
                .then()
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .statusCode(200)
                .extract()
                .body()
                .asString();

        EmployeeDTO createdDTO = objectMapper.readValue(content, EmployeeDTO.class);
        employeeDTO = createdDTO;

        assertNotNull(createdDTO.getId());
        assertTrue(createdDTO.getId() > 0);

        assertEquals("MockRegister", createdDTO.getEmployeeRegister());
        assertEquals("MockEmployee", createdDTO.getName());
        assertEquals("mockemail@email.com", createdDTO.getEmail());
    }

    @Test
    @Order(2)
    void update() throws JsonProcessingException {
        employeeDTO.setName("NewMockEmployeeName");

        var content = given(specification)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .accept(MediaType.APPLICATION_XML_VALUE)
                .body(employeeDTO)
                .when()
                .put()
                .then()
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .statusCode(200)
                .extract()
                .body()
                .asString();

        EmployeeDTO updatedDTO = objectMapper.readValue(content, EmployeeDTO.class);
        employeeDTO = updatedDTO;

        assertNotNull(updatedDTO.getId());
        assertTrue(updatedDTO.getId() > 0);

        assertEquals("MockRegister", updatedDTO.getEmployeeRegister());
        assertEquals("NewMockEmployeeName", updatedDTO.getName());
        assertEquals("mockemail@email.com", updatedDTO.getEmail());
    }

    @Test
    @Order(3)
    void findById() throws JsonProcessingException {
        var content = given(specification)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .accept(MediaType.APPLICATION_XML_VALUE)
                .pathParam("id", employeeDTO.getId())
                .when()
                .get("{id}")
                .then()
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .statusCode(200)
                .extract()
                .body()
                .asString();

        EmployeeDTO foundDTO = objectMapper.readValue(content, EmployeeDTO.class);
        employeeDTO = foundDTO;

        assertNotNull(foundDTO.getId());
        assertTrue(foundDTO.getId() > 0);

        assertEquals("MockRegister", foundDTO.getEmployeeRegister());
        assertEquals("NewMockEmployeeName", foundDTO.getName());
        assertEquals("mockemail@email.com", foundDTO.getEmail());
    }

    @Test
    @Order(4)
    void delete() {
        given(specification)
                .pathParam("id", employeeDTO.getId())
                .when()
                .delete("{id}")
                .then()
                .statusCode(204);
    }

    @Test
    @Order(5)
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

        PagedModelEmployee wrapper = objectMapper.readValue(content, PagedModelEmployee.class);
        List<EmployeeDTO> employees = wrapper.getContent();

        EmployeeDTO employeeOne = employees.get(0);
        assertNotNull(employeeOne.getId());
        assertTrue(employeeOne.getId() > 0);
        assertEquals("Fernanda Lima", employeeOne.getName());
        assertEquals("EMP001", employeeOne.getEmployeeRegister());
        assertEquals("fernanda.lima@example.com", employeeOne.getEmail());

        EmployeeDTO employeeThree = employees.get(2);
        assertNotNull(employeeThree.getId());
        assertTrue(employeeThree.getId() > 0);
        assertEquals("Francesco", employeeThree.getName());
        assertEquals("561813169", employeeThree.getEmployeeRegister());
        assertEquals("feveleigh1v@istockphoto.com", employeeThree.getEmail());
    }

    private void mockEmployee() {
        employeeDTO.setEmployeeRegister("MockRegister");
        employeeDTO.setName("MockEmployee");
        employeeDTO.setEmail("mockemail@email.com");
    }
}
