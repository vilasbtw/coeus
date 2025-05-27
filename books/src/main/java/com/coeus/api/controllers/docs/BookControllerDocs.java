package com.coeus.api.controllers.docs;

import com.coeus.api.models.dtos.BookDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface BookControllerDocs {

    @Operation(
        summary = "Adds a book",
        description = "Adds a new book by passing in a JSON, XML or YML representation of the book.",
        tags = "Books",
        responses = {
            @ApiResponse(
                description = "Success",
                responseCode = "200",
                content = @Content(schema = @Schema(implementation = BookDTO.class))
            ),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
        }
    )
    BookDTO create(@RequestBody BookDTO bookDTO);

    @Operation(
        summary = "Finds a book",
        description = "Finds a specific book by its id",
        tags = "Books",
        responses = {
            @ApiResponse(
                description = "Success",
                responseCode = "200",
                content = @Content(schema = @Schema(implementation = BookDTO.class))
            ),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
        }
    )
    BookDTO findById(@PathVariable(value = "id") Long id);

    @Operation(
        summary = "Finds all books",
        tags = "Books",
        responses = {
            @ApiResponse(
                description = "Success",
                responseCode = "200",
                content = {
                    @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        array = @ArraySchema(schema = @Schema(implementation = BookDTO.class))
                    )
                }
            ),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
        }
    )
    List<BookDTO> findAll();

    @Operation(
        summary = "Updates a book",
        description = "Updates a book info by passing in a JSON, XML or YML representation of the updated book.",
        tags = "Books",
        responses = {
            @ApiResponse(
                description = "Success",
                responseCode = "200",
                content = @Content(schema = @Schema(implementation = BookDTO.class))
            ),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
        }
    )
    BookDTO update(@RequestBody BookDTO bookDTO);

    @Operation(
        summary = "Deletes a book",
        description = "Deletes a book by its id.",
        tags = "Books",
        responses = {
            @ApiResponse(
                description = "Success",
                responseCode = "200",
                content = @Content(schema = @Schema(implementation = BookDTO.class))
            ),
            @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
        }
    )
    ResponseEntity<?> delete(@PathVariable(value = "id") Long id);
}