package com.coeus.api.repositories;

import com.coeus.api.integrationtests.testcontainers.AbstractIntegrationTest;
import com.coeus.api.models.Student;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

// prevents Spring from replacing the real database with a test database.
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
// integrates JUnit 5 with Spring.
@ExtendWith(SpringExtension.class)
// configures the test to focus only on JPA repository layer.
@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class StudentRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    StudentRepository repository;
    private static Student student;

    @BeforeAll
    static void setUp() {
        student = new Student();
    }

    @Test
    @Order(1)
    void findStudentsByName() {
        Pageable pageable = PageRequest.of(
                0,
                12,
                Sort.by(Sort.Direction.ASC, "name"));

        student = repository.findStudentsByName("and", pageable).getContent().get(0);

        assertNotNull(student);
        assertNotNull(student.getId());

        assertEquals("093855501", student.getStudentRegister());
        assertEquals("Aleksandr", student.getName());
        assertEquals("amouserkt@fastcompany.com", student.getEmail());
        assertEquals("Medicine", student.getCourse());
        assertTrue(student.isEnabled());
    }

    @Test
    @Order(2)
    void disableStudent() {

        Long id = student.getId();
        repository.disableStudent(id);

        var result = repository.findById(id);
        student = result.get();

        assertNotNull(student);
        assertNotNull(student.getId());

        assertEquals("093855501", student.getStudentRegister());
        assertEquals("Aleksandr", student.getName());
        assertEquals("amouserkt@fastcompany.com", student.getEmail());
        assertEquals("Medicine", student.getCourse());
        assertFalse(student.isEnabled());
    }

}