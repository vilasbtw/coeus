package com.coeus.api.controllers.docs;

import com.coeus.api.models.dtos.security.*;
import com.coeus.api.models.security.user.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthorizationControllerDocs {

    @Operation(
        summary = "Authenticates a user and returns access + refresh tokens",
        description = "Validates username and password, then generates access and refresh tokens for the authenticated user.",
        tags = "Authentication",
        responses = {
            @ApiResponse(
                description = "Success",
                responseCode = "200",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = TokenDTO.class)
                )),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
        }
    )
    ResponseEntity<TokenDTO> login(@RequestBody @Valid AuthenticationDTO data);

    @Operation(
        summary = "Refreshes an Access Token",
        description = "Generates a new access token using a valid refresh token",
        tags = "Authentication",
        responses = {
            @ApiResponse(
                description = "Success",
                responseCode = "200",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = RefreshTokenDTO.class))
            ),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
        }
    )
    ResponseEntity<RefreshTokenDTO> refreshToken(@RequestBody RefreshTokenDTO request);

    @Operation(
            summary = "Creates a new User",
            description = "Registers a new user with username and password.",
            tags = "Users",
            responses = {
                    @ApiResponse(description = "Created", responseCode = "201", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    ResponseEntity<?> register(RegisterDTO data);

    @Operation(
            summary = "Revokes all refresh tokens of the authenticated user",
            description = "Performs logout by removing all refresh tokens associated with the logged-in user. Requires a valid access token.",
            tags = "Authentication",
            responses = {
                    @ApiResponse(description = "Logout successful", responseCode = "200", content = @Content),
                    @ApiResponse(description = "Invalid token", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    ResponseEntity<?> logout(@AuthenticationPrincipal User user);

}
