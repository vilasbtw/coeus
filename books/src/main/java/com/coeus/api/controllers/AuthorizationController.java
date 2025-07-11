package com.coeus.api.controllers;

import com.coeus.api.controllers.docs.AuthorizationControllerDocs;
import com.coeus.api.models.dtos.AuthenticationDTO;
import com.coeus.api.models.dtos.RegisterDTO;
import com.coeus.api.models.dtos.TokenDTO;
import com.coeus.api.models.user.User;
import com.coeus.api.repositories.UserRepository;
import com.coeus.api.security.JwtTokenProvider;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

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
    JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthenticationDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.getUsername(), data.getPassword());
        var authentication = authenticationManager.authenticate(usernamePassword);
        var token = jwtTokenProvider.generateToken((User) authentication.getPrincipal());
        return ResponseEntity.ok(new TokenDTO(token));
    }
    @PostMapping(value = "/register", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public ResponseEntity<?> register(@RequestBody @Valid RegisterDTO data) {
        if (repository.findByUsername(data.getUsername()).isPresent()) {
            return ResponseEntity
                .badRequest()
                .body("This username is already in use. Please pick another one.");
        }

        String encryptedPassword = passwordEncoder.encode(data.getPassword());
        User newUser = new User(data.getUsername(), encryptedPassword, data.getRole(), data.getEmployeeId());

        repository.save(newUser);
        return ResponseEntity.ok().build();
    }
}
