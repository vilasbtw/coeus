package com.coeus.api.controllers.security;

import com.coeus.api.controllers.docs.AuthorizationControllerDocs;
import com.coeus.api.models.dtos.security.*;
import com.coeus.api.models.security.user.User;
import com.coeus.api.repositories.security.UserRepository;
import com.coeus.api.security.JwtTokenProvider;
import com.coeus.api.services.security.RefreshTokenService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

@Tag(name = "Authentication Endpoint")
@RestController
@RequestMapping("/auth")
public class AuthorizationController implements AuthorizationControllerDocs {

    @Autowired
    private UserRepository repository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @PostMapping("/login")
    public ResponseEntity<AuthTokensDTO> login(@RequestBody @Valid AuthenticationDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.getUsername(), data.getPassword());
        var authentication = authenticationManager.authenticate(usernamePassword);
        var user = (User) authentication.getPrincipal();

        String accessToken = jwtTokenProvider.generateToken(user);
        String refreshToken = refreshTokenService.generateToken(user).getToken();

        return ResponseEntity.ok(new AuthTokensDTO(accessToken, refreshToken));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<TokenDTO> refreshToken(@RequestBody RefreshTokenDTO request) {
        String refreshToken = request.getRefreshToken();
        String newAccessToken = refreshTokenService.processRefreshToken(refreshToken);

        return ResponseEntity.ok(new TokenDTO(newAccessToken));
    }

    @PostMapping(value = "/register", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public ResponseEntity<?> register(@RequestBody @Valid RegisterDTO data) {
        if (repository.findByUsername(data.getUsername()).isPresent()) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("This username is already in use. Please pick another one.");
        }

        String encryptedPassword = passwordEncoder.encode(data.getPassword());
        User newUser = new User(data.getUsername(), encryptedPassword, data.getRole(), data.getEmployeeId());

        repository.save(newUser);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .build();
    }
}
