package com.coeus.books.models.mapper;

import com.coeus.books.models.Student;
import com.coeus.books.models.dtos.StudentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StudentMapper {
    Student toEntity(StudentDTO bookDTO);
    StudentDTO toDTO(Student book);
    List<Student> toEntities(List<StudentDTO> bookDTOS);
    List<StudentDTO> toDTOS(List<Student> books);
}