package com.coeus.api.integrationtests.dto.wrapper.json;

import com.coeus.api.integrationtests.dto.BookDTO;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class BookEmbeddedDTO {

    @JsonProperty("books")
    private List<BookDTO> books;

    public BookEmbeddedDTO() {
    }

    public List<BookDTO> getBooks() {
        return books;
    }

    public void setBooks(List<BookDTO> books) {
        this.books = books;
    }
}