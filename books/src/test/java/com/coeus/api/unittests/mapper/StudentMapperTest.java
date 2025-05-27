package com.coeus.api.unittests.mapper;

import com.coeus.api.models.Student;
import com.coeus.api.models.dtos.StudentDTO;
import com.coeus.api.models.mapper.StudentMapper;
import com.coeus.api.unittests.mocks.MockStudent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StudentMapperTest {

    MockStudent mocker;
    StudentMapper mapper;

    @BeforeEach
    public void setUp() {
        mocker = new MockStudent();
        mapper = Mappers.getMapper(StudentMapper.class);
    }

    @Test
    public void parseEntityToDTOTest() {
        StudentDTO studentDTO = mapper.toDTO(mocker.mockStudentEntity());
        assertEquals("Student register: 0", studentDTO.getStudentRegister());
        assertEquals("Name: 0", studentDTO.getName());
        assertEquals("Email: 0", studentDTO.getEmail());
        assertEquals("Course: 0", studentDTO.getCourse());
    }

    @Test
    public void parseDTOToEntityTest() {
        Student student = mapper.toEntity(mocker.mockStudentDTO());
        assertEquals("Student register: 0", student.getStudentRegister());
        assertEquals("Name: 0", student.getName());
        assertEquals("Email: 0", student.getEmail());
        assertEquals("Course: 0", student.getCourse());
    }

    @Test
    public void parseEntitiesToDTOSTest() {
        List<StudentDTO> studentDTOS = mapper.toDTOS(mocker.mookStudentEntities());

        StudentDTO studentDTO2 = studentDTOS.get(2);
        assertEquals("Student register: 2", studentDTO2.getStudentRegister());
        assertEquals("Name: 2", studentDTO2.getName());
        assertEquals("Email: 2", studentDTO2.getEmail());
        assertEquals("Course: 2", studentDTO2.getCourse());

        StudentDTO studentDTO6 = studentDTOS.get(6);
        assertEquals("Student register: 6", studentDTO6.getStudentRegister());
        assertEquals("Name: 6", studentDTO6.getName());
        assertEquals("Email: 6", studentDTO6.getEmail());
        assertEquals("Course: 6", studentDTO6.getCourse());

        StudentDTO studentDTO11 = studentDTOS.get(11);
        assertEquals("Student register: 11", studentDTO11.getStudentRegister());
        assertEquals("Name: 11", studentDTO11.getName());
        assertEquals("Email: 11", studentDTO11.getEmail());
        assertEquals("Course: 11", studentDTO11.getCourse());
    }

    @Test
    public void parseDTOSToEntitiesTest() {
        List<Student> students = mapper.toEntities(mocker.mookStudentDTOS());

        Student student2 = students.get(2);
        assertEquals("Student register: 2", student2.getStudentRegister());
        assertEquals("Name: 2", student2.getName());
        assertEquals("Email: 2", student2.getEmail());
        assertEquals("Course: 2", student2.getCourse());

        Student student6 = students.get(6);
        assertEquals("Student register: 6", student6.getStudentRegister());
        assertEquals("Name: 6", student6.getName());
        assertEquals("Email: 6", student6.getEmail());
        assertEquals("Course: 6", student6.getCourse());

        Student student11 = students.get(11);
        assertEquals("Student register: 11", student11.getStudentRegister());
        assertEquals("Name: 11", student11.getName());
        assertEquals("Email: 11", student11.getEmail());
        assertEquals("Course: 11", student11.getCourse());
    }

}