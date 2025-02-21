package com.coeus.books.controllers;

import com.coeus.books.services.BookService;
import com.coeus.books.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService service;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE,
                 consumes = MediaType.APPLICATION_JSON_VALUE)
    public Book create(@RequestBody Book book) {
        return service.create(book);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Book findById(@PathVariable(value = "id") Long id) {
        return service.findById(id);
    }
}