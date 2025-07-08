package com.coeus.api.integrationtests.dto.wrapper.json;

import com.coeus.api.integrationtests.dto.StudentDTO;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class StudentEmbeddedDTO {

    @JsonProperty("students")
    private List<StudentDTO> students;

    public StudentEmbeddedDTO() {
    }

    public List<StudentDTO> getStudents() {
        return students;
    }

    public void setStudents(List<StudentDTO> students) {
        this.students = students;
    }
}