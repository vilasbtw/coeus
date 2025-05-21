package com.coeus.books.models.dtos;

// Record are basic classes that has no business logic and used only for representing data
// Very similar to Data Classes

// To retrieve a value from a record class, we don't call getters, but the attributes instead, e.g.:
// Instead of bookDTO.getId(), you should call bookDTO.id() only.
public record BookDTO(
        Long id,
        String bookName,
        String authorName,
        String publisherName,
        int numberOfPages,
        String genre,
        double price
){}