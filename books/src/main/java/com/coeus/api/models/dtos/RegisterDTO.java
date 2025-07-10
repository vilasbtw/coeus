package com.coeus.api.models.dtos;

import com.coeus.api.models.user.UserRole;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RegisterDTO {

    private String username;

    private String password;

    private UserRole role;

    @JsonProperty("employee_id")
    private Long employeeId;

    public RegisterDTO() {
    }

    public RegisterDTO(String username, String password, UserRole role, Long employeeId) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.employeeId = employeeId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }
}
