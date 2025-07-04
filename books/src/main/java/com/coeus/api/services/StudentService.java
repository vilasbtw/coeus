package com.coeus.api.services;

import com.coeus.api.controllers.StudentController;
import com.coeus.api.exceptions.RequiredObjectIsNullException;
import com.coeus.api.exceptions.ResourceNotFoundException;
import com.coeus.api.models.Student;
import com.coeus.api.models.dtos.StudentDTO;
import com.coeus.api.models.mapper.StudentMapper;
import com.coeus.api.repositories.StudentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class StudentService {

    private final StudentRepository repository;
    private final StudentMapper mapper;

    @Autowired
    public StudentService(StudentRepository repository, StudentMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public StudentDTO create(StudentDTO studentDTO) {

        if (studentDTO == null) throw new RequiredObjectIsNullException();

        Student entity = mapper.toEntity(studentDTO);
        Student persistedStudent = repository.save(entity);
        StudentDTO dto = mapper.toDTO(persistedStudent);
        addHateoasLinks(dto);
        return dto;
    }

    public StudentDTO findById(Long id) {
        Student entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("This resource could not be found."));

        StudentDTO studentDTO = mapper.toDTO(entity);
        addHateoasLinks(studentDTO);
        return studentDTO;
    }

    public List<StudentDTO> findAll() {
        List<Student> studentsList = repository.findAll();
        List<StudentDTO> dtosList = new ArrayList<>();

        for (Student student : studentsList) {
            dtosList.add(mapper.toDTO(student));
        }

        for (StudentDTO dto : dtosList) {
            addHateoasLinks(dto);
        }
        return dtosList;
    }

    public StudentDTO update(StudentDTO studentDTO) {

        if (studentDTO == null) throw new RequiredObjectIsNullException();

        Student entity = repository.findById(studentDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundException("This resource could not be found."));

        entity.setStudentRegister(studentDTO.getStudentRegister());
        entity.setName(studentDTO.getName());
        entity.setEmail(studentDTO.getEmail());
        entity.setCourse(studentDTO.getCourse());

        repository.save(entity);
        StudentDTO dto = mapper.toDTO(entity);
        addHateoasLinks(dto);
        return dto;
    }

    // this method is not provided by JPA.
    // so we must add @Transation to ensure ACID is being followed.
    @Transactional
    public StudentDTO disableStudent(Long id) {
        repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("This resource could not be found."));

        repository.disableStudent(id);

        Student entity = repository.findById(id).get();
        StudentDTO dto = mapper.toDTO(entity);
        addHateoasLinks(dto);
        return dto;
    }

    public void delete(Long id) {
        Student entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("This resource could not be found."));

        repository.delete(entity);
    }

    private static void addHateoasLinks(StudentDTO studentDTO) {
        studentDTO.add(linkTo(methodOn(StudentController.class).create(studentDTO)).withRel("create").withType("POST"));
        studentDTO.add(linkTo(methodOn(StudentController.class).findById(studentDTO.getId())).withSelfRel().withType("GET"));
        studentDTO.add(linkTo(methodOn(StudentController.class).findAll()).withRel("findAll").withType("GET"));
        studentDTO.add(linkTo(methodOn(StudentController.class).update(studentDTO)).withRel("update").withType("PUT"));
        studentDTO.add(linkTo(methodOn(StudentController.class).disableStudent(studentDTO)).withRel("disable").withType("PATCH"));
        studentDTO.add(linkTo(methodOn(StudentController.class).delete(studentDTO.getId())).withRel("delete").withType("DELETE"));
    }
}