package com.coeus.api.integrationtests.dto;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class AuthenticationDTO {

    private String username;
    private String password;

    public AuthenticationDTO() {
    }

    public AuthenticationDTO(String username, String password) {
        this.username = username;
        this.password = password;
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
}
