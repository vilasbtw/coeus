package com.coeus.api.controllers.security;

import com.coeus.api.controllers.docs.UserControllerDocs;
import com.coeus.api.models.dtos.security.UserStatusDTO;
import com.coeus.api.services.security.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController implements UserControllerDocs {

    @Autowired
    private UserService userService;

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateUserStatus(
            @PathVariable Long id,
            @RequestBody UserStatusDTO statusDTO
    ) {
        userService.updateUserStatus(id, statusDTO);
        return ResponseEntity.noContent().build();
    }
}
