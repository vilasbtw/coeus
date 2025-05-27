package com.coeus.api.unittests.mocks;

import com.coeus.api.models.Student;
import com.coeus.api.models.dtos.StudentDTO;

import java.util.ArrayList;
import java.util.List;

public class MockStudent {

    public Student mockStudentEntity() {
        return mockStudentEntity(0);
    }

    public Student mockStudentEntity(Integer number) {
        Student student = new Student();

        student.setId(number.longValue());
        student.setStudentRegister("Student register: " + number);
        student.setName("Name: " + number);
        student.setEmail("Email: " + number);
        student.setCourse("Course: " + number);
        return student;
    }

    public List<Student> mookStudentEntities() {
        List<Student> students = new ArrayList<>();

        for (int i = 0; i < 15; i++) {
            students.add(mockStudentEntity(i));
        }
        return students;
    }

    public StudentDTO mockStudentDTO() {
        return mockStudentDTO(0);
    }

    public StudentDTO mockStudentDTO(Integer number) {
        StudentDTO studentDTO = new StudentDTO();

        studentDTO.setId(number.longValue());
        studentDTO.setStudentRegister("Student register: " + number);
        studentDTO.setName("Name: " + number);
        studentDTO.setEmail("Email: " + number);
        studentDTO.setCourse("Course: " + number);
        return studentDTO;
    }

    public List<StudentDTO> mookStudentDTOS() {
        List<StudentDTO> studentDTO = new ArrayList<>();

        for (int i = 0; i < 15; i++) {
            studentDTO.add(mockStudentDTO(i));
        }
        return studentDTO;
    }

}