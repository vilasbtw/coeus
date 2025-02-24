package com.coeus.books.services;

import com.coeus.books.dto.BookDTO;
import com.coeus.books.exceptions.ResourceNotFoundException;
import com.coeus.books.mapper.Mapper;
import com.coeus.books.repositories.BookRepository;
import com.coeus.books.models.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {

    @Autowired
    private BookRepository repository;
    private Mapper mapper;

    public Book create(Book book) {
        return repository.save(book);
    }

    public BookDTO findById(Long id) {
        return repository.findById(id)
                .map(mapper)
                .orElseThrow(() -> new ResourceNotFoundException("This resource could not be found."));
    }

    public List<BookDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper).collect(Collectors.toList());
    }

    public Book update(Book book) {
        Book entity = findById(book.getId());

        entity.setId(book.getId());
        entity.setBookName(book.getBookName());
        entity.setAuthorName(book.getAuthorName());
        entity.setPublisherName(book.getPublisherName());
        entity.setNumberOfPages(book.getNumberOfPages());
        entity.setGenre(book.getGenre());
        entity.setPrice(book.getPrice());

        return repository.save(book);
    }

    public void delete(Long id) {
        Book entity = findById(id);
        repository.delete(entity);
    }
}