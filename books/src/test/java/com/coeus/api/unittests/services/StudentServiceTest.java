package com.coeus.api.unittests.services;

import com.coeus.api.exceptions.RequiredObjectIsNullException;
import com.coeus.api.models.Student;
import com.coeus.api.models.dtos.StudentDTO;
import com.coeus.api.models.mapper.StudentMapper;
import com.coeus.api.repositories.StudentRepository;
import com.coeus.api.services.StudentService;
import com.coeus.api.unittests.mocks.MockStudent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    MockStudent input;
    StudentService service;
    @Mock
    StudentRepository repository;
    StudentMapper mapper;

    @Mock
    PagedResourcesAssembler<StudentDTO> assembler;

    @BeforeEach
    void setUp() {
        input = new MockStudent();
        mapper = Mappers.getMapper(StudentMapper.class);
        service = new StudentService(repository, mapper, assembler);
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
                    && link.getHref().contains("/students")
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
                    && link.getHref().contains("/students")
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
                    && link.getHref().contains("/students")
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
        List<Student> entityList = input.mookStudentEntities();
        Page<Student> entityPage = new PageImpl<>(entityList);
        when(repository.findAll(any(Pageable.class))).thenReturn(entityPage);

        when(assembler.toModel(any(Page.class), any(Link.class))).thenAnswer(invocation -> {
            Page<StudentDTO> dtoPage = invocation.getArgument(0);
            List<EntityModel<StudentDTO>> models = dtoPage.getContent().stream()
                    .map(EntityModel::of)
                    .collect(Collectors.toList());
            PagedModel.PageMetadata metadata = new PagedModel.PageMetadata(
                dtoPage.getSize(),
                dtoPage.getNumber(),
                dtoPage.getTotalElements()
            );
            return PagedModel.of(models, metadata);
        });

        Pageable pageable = PageRequest.of(0, 15);

        PagedModel<EntityModel<StudentDTO>> result = service.findAll(pageable);

        assertNotNull(result);
        List<StudentDTO> students = result.getContent().stream()
                .map(EntityModel::getContent)
                .collect(Collectors.toList());

        assertNotNull(students);
        assertEquals(15, students.size());

        validateIndividualStudent(students.get(1), 1);
        validateIndividualStudent(students.get(5), 5);
        validateIndividualStudent(students.get(10), 10);
    }

    private void validateIndividualStudent(StudentDTO student, int i) {
        assertEquals("Student register: " + i, student.getStudentRegister());
        assertEquals("Name: " + i, student.getName());
        assertEquals("Email: " + i, student.getEmail());
        assertEquals("Course: " + i, student.getCourse());

        assertNotNull(student);
        assertNotNull(student.getId());
        assertNotNull(student.getLinks());

        assertTrue(student.getLink("self").isPresent());
        assertTrue(student.getLink("self").get().getHref().endsWith("/students/" + i));

        assertTrue(student.getLink("findAll").isPresent());
        assertTrue(student.getLink("findAll").get().getHref().contains("/students"));

        assertTrue(student.getLink("create").isPresent());
        assertTrue(student.getLink("create").get().getHref().endsWith("/students"));

        assertTrue(student.getLink("update").isPresent());
        assertTrue(student.getLink("update").get().getHref().endsWith("/students"));

        assertTrue(student.getLink("delete").isPresent());
        assertTrue(student.getLink("delete").get().getHref().endsWith("/students/" + i));
    }

}