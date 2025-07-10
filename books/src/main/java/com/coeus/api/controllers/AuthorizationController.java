package com.coeus.api.controllers;

import com.coeus.api.models.dtos.AuthenticationDTO;
import com.coeus.api.models.dtos.RegisterDTO;
import com.coeus.api.models.user.User;
import com.coeus.api.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthorizationController {

    @Autowired
    private UserRepository repository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthenticationDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.getUsername(), data.getPassword());
        var authentication = authenticationManager.authenticate(usernamePassword);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterDTO data) {
        if (repository.findByUsername(data.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().build();
        }

        String encryptedPassword = passwordEncoder.encode(data.getPassword());
        User newUser = new User(data.getUsername(), encryptedPassword, data.getRole(), data.getEmployeeId());

        repository.save(newUser);
        return ResponseEntity.ok().build();
    }
}
