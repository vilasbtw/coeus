package com.coeus.api.models.security.user;

public enum UserRole {
    LIBRARIAN("ROLE_LIBRARIAN"),
    COORDINATOR("ROLE_COORDINATOR");

    private final String authority;

    UserRole(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return authority;
    }
}