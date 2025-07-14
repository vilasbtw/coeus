package com.coeus.api.integrationtests.dto;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RefreshTokenDTO {
    private String refreshToken;

    public RefreshTokenDTO() {
    }

    public RefreshTokenDTO(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}

