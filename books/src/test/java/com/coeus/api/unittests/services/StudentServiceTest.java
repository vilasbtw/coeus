package com.coeus.api.unittests.services;

import com.coeus.api.exceptions.RequiredObjectIsNullException;
import com.coeus.api.models.Student;
import com.coeus.api.models.dtos.StudentDTO;
import com.coeus.api.models.mapper.StudentMapper;
import com.coeus.api.repositories.StudentRepository;
import com.coeus.api.services.StudentService;
import com.coeus.api.unittests.mocks.MockStudent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    MockStudent input;
    StudentService service;
    @Mock
    StudentRepository repository;
    StudentMapper mapper;

    @BeforeEach
    void setUp() {
        input = new MockStudent();
        mapper = Mappers.getMapper(StudentMapper.class);
        service = new StudentService(repository, mapper);
    }

    @Test
    void findById() {
        Student student = input.mockStudentEntity(1);
        when(repository.findById(1L)).thenReturn(Optional.of(student));

        StudentDTO result = service.findById(1L);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("self")
                        && link.getHref().endsWith("/students/1")
                        && link.getType().equals("GET")
                )
        );

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("findAll")
                        && link.getHref().endsWith("/students")
                        && link.getType().equals("GET")
                )
        );

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create")
                        && link.getHref().endsWith("/students")
                        && link.getType().equals("POST")
                )
        );

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update")
                        && link.getHref().endsWith("/students")
                        && link.getType().equals("PUT")
                )
        );

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete")
                        && link.getHref().endsWith("/students/1")
                        && link.getType().equals("DELETE")
                )
        );

        assertEquals("Student register: 1", student.getStudentRegister());
        assertEquals("Name: 1", student.getName());
        assertEquals("Email: 1", student.getEmail());
        assertEquals("Course: 1", student.getCourse());
    }

    @Test
    void testCreateWithNullStudent() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class,
                () -> {
                    service.create(null);
                });

        String expectedMessage = "It is not allowed to persist a null object!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void create() {
        Student student = input.mockStudentEntity(1);
        Student persisted = student;

        StudentDTO dto = input.mockStudentDTO(1);

        when(repository.save(student)).thenReturn(persisted);

        StudentDTO result = service.create(dto);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("self")
                        && link.getHref().endsWith("/students/1")
                        && link.getType().equals("GET")
                )
        );

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("findAll")
                        && link.getHref().endsWith("/students")
                        && link.getType().equals("GET")
                )
        );

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create")
                        && link.getHref().endsWith("/students")
                        && link.getType().equals("POST")
                )
        );

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update")
                        && link.getHref().endsWith("/students")
                        && link.getType().equals("PUT")
                )
        );

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete")
                        && link.getHref().endsWith("/students/1")
                        && link.getType().equals("DELETE")
                )
        );

        assertEquals("Student register: 1", student.getStudentRegister());
        assertEquals("Name: 1", student.getName());
        assertEquals("Email: 1", student.getEmail());
        assertEquals("Course: 1", student.getCourse());
    }

    @Test
    void testUpdateWithNullBook() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class,
                () -> {
                    service.update(null);
                });

        String expectedMessage = "It is not allowed to persist a null object!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void update() {
        Student student = input.mockStudentEntity(1);
        Student persisted = student;

        StudentDTO dto = input.mockStudentDTO(1);

        when(repository.findById(1L)).thenReturn(Optional.of(student));
        when(repository.save(student)).thenReturn(persisted);

        StudentDTO result = service.update(dto);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("self")
                        && link.getHref().endsWith("/students/1")
                        && link.getType().equals("GET")
                )
        );

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("findAll")
                        && link.getHref().endsWith("/students")
                        && link.getType().equals("GET")
                )
        );

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create")
                        && link.getHref().endsWith("/students")
                        && link.getType().equals("POST")
                )
        );

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update")
                        && link.getHref().endsWith("/students")
                        && link.getType().equals("PUT")
                )
        );

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete")
                        && link.getHref().endsWith("/students/1")
                        && link.getType().equals("DELETE")
                )
        );

        assertEquals("Student register: 1", student.getStudentRegister());
        assertEquals("Name: 1", student.getName());
        assertEquals("Email: 1", student.getEmail());
        assertEquals("Course: 1", student.getCourse());
    }

    @Test
    void delete() {
        Student student = input.mockStudentEntity(1);
        when(repository.findById(1L)).thenReturn(Optional.of(student));

        service.delete(1L);
    }

    @Test
    void findAll() {
        List<Student> list = input.mookStudentEntities();
        when(repository.findAll()).thenReturn(list);
        List<StudentDTO> dtos = new ArrayList<>();// service.findAll(pageable);

        assertNotNull(dtos);
        assertEquals(15, dtos.size());

        StudentDTO studentOne = dtos.get(1);

        assertTrue(studentOne.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("self")
                        && link.getHref().endsWith("/students/1")
                        && link.getType().equals("GET")
                )
        );

        assertTrue(studentOne.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("findAll")
                        && link.getHref().endsWith("/students")
                        && link.getType().equals("GET")
                )
        );

        assertTrue(studentOne.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create")
                        && link.getHref().endsWith("/students")
                        && link.getType().equals("POST")
                )
        );

        assertTrue(studentOne.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update")
                        && link.getHref().endsWith("/students")
                        && link.getType().equals("PUT")
                )
        );

        assertTrue(studentOne.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete")
                        && link.getHref().endsWith("/students/1")
                        && link.getType().equals("DELETE")
                )
        );

        StudentDTO studentSeven = dtos.get(7);

        assertTrue(studentSeven.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("self")
                        && link.getHref().endsWith("/students/7")
                        && link.getType().equals("GET")
                )
        );

        assertTrue(studentSeven.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("findAll")
                        && link.getHref().endsWith("/students")
                        && link.getType().equals("GET")
                )
        );

        assertTrue(studentSeven.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create")
                        && link.getHref().endsWith("/students")
                        && link.getType().equals("POST")
                )
        );

        assertTrue(studentSeven.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update")
                        && link.getHref().endsWith("/students")
                        && link.getType().equals("PUT")
                )
        );

        assertTrue(studentSeven.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete")
                        && link.getHref().endsWith("/students/7")
                        && link.getType().equals("DELETE")
                )
        );

        assertEquals("Student register: 7", studentSeven.getStudentRegister());
        assertEquals("Name: 7", studentSeven.getName());
        assertEquals("Email: 7", studentSeven.getEmail());
        assertEquals("Course: 7", studentSeven.getCourse());

        StudentDTO studentThirteen = dtos.get(13);

        assertTrue(studentThirteen.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("self")
                        && link.getHref().endsWith("/students/13")
                        && link.getType().equals("GET")
                )
        );

        assertTrue(studentThirteen.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("findAll")
                        && link.getHref().endsWith("/students")
                        && link.getType().equals("GET")
                )
        );

        assertTrue(studentThirteen.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create")
                        && link.getHref().endsWith("/students")
                        && link.getType().equals("POST")
                )
        );

        assertTrue(studentThirteen.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update")
                        && link.getHref().endsWith("/students")
                        && link.getType().equals("PUT")
                )
        );

        assertTrue(studentThirteen.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete")
                        && link.getHref().endsWith("/students/13")
                        && link.getType().equals("DELETE")
                )
        );

        assertEquals("Student register: 13", studentThirteen.getStudentRegister());
        assertEquals("Name: 13", studentThirteen.getName());
        assertEquals("Email: 13", studentThirteen.getEmail());
        assertEquals("Course: 13", studentThirteen.getCourse());
    }

}