package com.coeus.books.models.dtos;

// Record are basic classes that has no business logic and used only for representing data
// Very similar to Data Classes

import lombok.Builder;

// (toBuilder = true) allows increment or decrement attributes in the object BookDTO
@Builder(toBuilder = true)
public record BookDTO(
        Long id,
        String bookName,
        String authorName,
        String publisherName,
        int numberOfPages,
        String genre,
        double price
){}