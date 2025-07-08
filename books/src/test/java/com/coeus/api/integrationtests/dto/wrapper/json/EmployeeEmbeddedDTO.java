package com.coeus.api.integrationtests.dto.wrapper.json;

import com.coeus.api.integrationtests.dto.EmployeeDTO;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class EmployeeEmbeddedDTO {

    @JsonProperty("employees")
    private List<EmployeeDTO> employees;

    public EmployeeEmbeddedDTO() {
    }

    public List<EmployeeDTO> getEmployees() {
        return employees;
    }

    public void setEmployees(List<EmployeeDTO> employees) {
        this.employees = employees;
    }
}