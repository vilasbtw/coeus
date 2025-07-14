package com.coeus.api.repositories.security;

import com.coeus.api.models.security.auth.RefreshToken;
import com.coeus.api.models.security.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    // using optional because the token might not exist in the database, avoiding NullPointerException exceptions.
    Optional<RefreshToken> findByToken(String token);

    // logout
    void deleteAllByUser(User user);
}
