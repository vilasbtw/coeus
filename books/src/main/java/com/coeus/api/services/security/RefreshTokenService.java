package com.coeus.api.services.security;

import com.coeus.api.models.security.auth.RefreshToken;
import com.coeus.api.models.security.user.User;
import com.coeus.api.repositories.security.RefreshTokenRepository;
import com.coeus.api.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private static final int EXPIRATION_DAYS = 7;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    public RefreshToken generateToken(User user) {
        String token = UUID.randomUUID().toString();
        LocalDateTime expiration = LocalDateTime.now().plusDays(EXPIRATION_DAYS);

        RefreshToken refreshToken = new RefreshToken(token, expiration, user);
        return refreshTokenRepository.save(refreshToken);
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public boolean isValid(RefreshToken token) {
        return token.getExpirationDate().isAfter(LocalDateTime.now());
    }

    public void deleteByUser(User user) {
        refreshTokenRepository.deleteAllByUser(user);
    }

    public String processRefreshToken(String token) {
        RefreshToken refreshToken = findByToken(token)
            .orElseThrow(() -> new RuntimeException("Invalid token."));

        if (!isValid(refreshToken)) {
            refreshTokenRepository.delete(refreshToken);
            throw new RuntimeException("Expired token.");
        }

        User user = refreshToken.getUser();
        refreshTokenRepository.delete(refreshToken);
        
        return jwtTokenProvider.generateToken(user);
    }
}
