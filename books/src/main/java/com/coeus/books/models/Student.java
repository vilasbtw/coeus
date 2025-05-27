package com.coeus.books.models;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "student")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "register", nullable = false)
    private String studentRegister;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String course;

    public Student() {}

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(id, student.id) && Objects.equals(studentRegister, student.studentRegister) && Objects.equals(name, student.name) && Objects.equals(email, student.email) && Objects.equals(course, student.course);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, studentRegister, name, email, course);
    }
}