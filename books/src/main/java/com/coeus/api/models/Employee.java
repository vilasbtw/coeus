package com.coeus.api.models;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "register", nullable = false)
    private String employeeRegister;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    // TO-DO:
    // Implement fields: password_hash and role for JWT and Spring Security

    public Employee() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmployeeRegister() {
        return employeeRegister;
    }

    public void setEmployeeRegister(String employeeRegister) {
        this.employeeRegister = employeeRegister;
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return Objects.equals(id, employee.id) && Objects.equals(employeeRegister, employee.employeeRegister) && Objects.equals(name, employee.name) && Objects.equals(email, employee.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, employeeRegister, name, email);
    }

}