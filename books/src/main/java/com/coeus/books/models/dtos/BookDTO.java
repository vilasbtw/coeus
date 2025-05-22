package com.coeus.books.models.dtos;

// Record are basic classes that has no business logic and used only for representing data
// Very similar to Data Classes

// To retrieve a value from a record class, we don't call getters, but the attributes instead, e.g.:
// Instead of bookDTO.getId(), you should call bookDTO.id() only.

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"book_name", "author_name", "publisher_name", "price", "number_of_pages", "id"})
public record BookDTO(
        Long id,
        @JsonProperty("book_name")
        String bookName,
        @JsonProperty("author_name")
        String authorName,
        @JsonProperty("publisher_name")
        String publisherName,
        @JsonProperty("number_of_pages")
        int numberOfPages,
        @JsonIgnore
        String genre,
        double price
){}