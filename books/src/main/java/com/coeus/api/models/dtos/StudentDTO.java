package com.coeus.api.models.dtos;

import org.springframework.hateoas.RepresentationModel;

import java.util.Objects;

public class StudentDTO extends RepresentationModel<StudentDTO> {

    private Long id;
    private String studentRegister;
    private String name;
    private String email;
    private String course;
    private Boolean enabled;

    public StudentDTO() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStudentRegister() {
        return studentRegister;
    }

    public void setStudentRegister(String studentRegister) {
        this.studentRegister = studentRegister;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        StudentDTO that = (StudentDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(studentRegister, that.studentRegister) && Objects.equals(name, that.name) && Objects.equals(email, that.email) && Objects.equals(course, that.course) && Objects.equals(enabled, that.enabled);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, studentRegister, name, email, course, enabled);
    }
}