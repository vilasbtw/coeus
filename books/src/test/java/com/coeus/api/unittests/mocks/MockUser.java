package com.coeus.api.unittests.mocks;

import com.coeus.api.models.security.user.User;
import com.coeus.api.models.security.user.UserRole;

public class MockUser {

    public User mockUserEntity(Long employeeId) {
        User user = new User();
        user.setId(1L);
        user.setUsername("chorao");
        user.setPassword("123");
        user.setRole(UserRole.COORDINATOR);
        user.setEmployeeId(employeeId);
        user.setAccountExpired(false);
        user.setAccountLocked(false);
        user.setCredentialsExpired(false);
        user.setEnabled(true);
        return user;
    }
}
