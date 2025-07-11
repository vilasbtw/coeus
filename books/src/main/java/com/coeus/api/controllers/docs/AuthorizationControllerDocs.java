package com.coeus.api.controllers.docs;

import com.coeus.api.models.dtos.AuthenticationDTO;
import com.coeus.api.models.dtos.RegisterDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface AuthorizationControllerDocs {

    @Operation(
            summary = "Authenticates a user and returns a token",
            description = "Validates username and password; generates an access token for authentication.",
            tags = {"Authentication"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    ResponseEntity<?> login(AuthenticationDTO data);

    @Operation(
            summary = "Creates a new User",
            description = "Registers a new user with username and password.",
            tags = {"User Management"},
            responses = {
                    @ApiResponse(description = "Created", responseCode = "201", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    ResponseEntity<?> register(RegisterDTO data);

}
