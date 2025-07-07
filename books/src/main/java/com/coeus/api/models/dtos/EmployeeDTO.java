package com.coeus.api.models.dtos;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.util.Objects;

// defines the name of HATEOAS collection as "employees"
@Relation(collectionRelation = "employees")
public class EmployeeDTO extends RepresentationModel<EmployeeDTO> {

    private Long id;
    private String employeeRegister;
    private String name;
    private String email;

    public EmployeeDTO() {}

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
        if (!super.equals(o)) return false;
        EmployeeDTO that = (EmployeeDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(employeeRegister, that.employeeRegister) && Objects.equals(name, that.name) && Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, employeeRegister, name, email);
    }
}