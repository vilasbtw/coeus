package com.coeus.books.services;

import com.coeus.books.controllers.BookController;
import com.coeus.books.exceptions.RequiredObjectIsNullException;
import com.coeus.books.exceptions.ResourceNotFoundException;
import com.coeus.books.models.dtos.BookDTO;
import com.coeus.books.models.mapper.BookMapper;
import com.coeus.books.repositories.BookRepository;
import com.coeus.books.models.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class BookService {

    private final BookRepository repository;
    private final BookMapper mapper;

    @Autowired
    public BookService(BookRepository repository, BookMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    // Repository only works with entity classes, so:
    // BookDTO is converted to Book, so repository can manipulate it
    // Then, after saving it, we will convert Book to BookDTO again and return it to the client
    public BookDTO create(BookDTO bookDTO) {

        if (bookDTO == null) throw new RequiredObjectIsNullException();

        Book entity = mapper.toEntity(bookDTO);
        Book persistedBook = repository.save(entity);
        BookDTO dto = mapper.toDTO(persistedBook);
        addHateoasLinks(dto);
        return dto;
    }

    public BookDTO findById(Long id) {
        Book entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("This resource could not be found."));

        BookDTO bookDTO = mapper.toDTO(entity);
        addHateoasLinks(bookDTO);
        return bookDTO;
    }

    public List<BookDTO> findAll() {
        List<Book> booksList = repository.findAll();
        List<BookDTO> dtosList = new ArrayList<>();

        // repository.findAll().forEach(book -> books.add(mapper.toDTO(book)));

        for (Book book : booksList) {
            dtosList.add(mapper.toDTO(book));
        }

        for (BookDTO dto : dtosList) {
            addHateoasLinks(dto);
        }
        return dtosList;
    }

    public BookDTO update(BookDTO bookDTO) {

        if (bookDTO == null) throw new RequiredObjectIsNullException();

        Book entity = repository.findById(bookDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundException("This resource could not be found."));

        entity.setBookName(bookDTO.getBookName());
        entity.setAuthorName(bookDTO.getAuthorName());
        entity.setPublisherName(bookDTO.getPublisherName());
        entity.setNumberOfPages(bookDTO.getNumberOfPages());
        entity.setGenre(bookDTO.getGenre());
        entity.setPrice(bookDTO.getPrice());

        repository.save(entity);
        BookDTO dto = mapper.toDTO(entity);
        addHateoasLinks(dto);
        return dto;
    }

    // delete method do not implement DTO pattern, since the method do not return an object
    public void delete(Long id) {
        Book entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("This resource could not be found."));

        repository.delete(entity);
    }

    private static void addHateoasLinks(BookDTO bookDTO) {
        bookDTO.add(linkTo(methodOn(BookController.class).create(bookDTO)).withRel("create").withType("POST"));
        bookDTO.add(linkTo(methodOn(BookController.class).findById(bookDTO.getId())).withSelfRel().withType("GET"));
        bookDTO.add(linkTo(methodOn(BookController.class).findAll()).withRel("findAll").withType("GET"));
        bookDTO.add(linkTo(methodOn(BookController.class).update(bookDTO)).withRel("update").withType("PUT"));
        bookDTO.add(linkTo(methodOn(BookController.class).delete(bookDTO.getId())).withRel("delete").withType("DELETE"));
    }
}