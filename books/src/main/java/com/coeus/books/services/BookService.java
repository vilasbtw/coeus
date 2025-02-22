package com.coeus.books.services;

import com.coeus.books.repositories.BookRepository;
import com.coeus.books.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookRepository repository;

    public Book create(Book book) {
        return repository.save(book);
    }

    public Book findById(Long id) {
        return repository.findById(id).orElseThrow();
    }

    public List<Book> findAll() {
        return repository.findAll();
    }

    public void delete(Long id) {
        Book entity = findById(id);
        repository.delete(entity);
    }
}