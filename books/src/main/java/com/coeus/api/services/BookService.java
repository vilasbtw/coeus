package com.coeus.api.services;

import com.coeus.api.controllers.BookController;
import com.coeus.api.exceptions.RequiredObjectIsNullException;
import com.coeus.api.exceptions.ResourceNotFoundException;
import com.coeus.api.models.dtos.BookDTO;
import com.coeus.api.models.mapper.BookMapper;
import com.coeus.api.repositories.BookRepository;
import com.coeus.api.models.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class BookService {

    private final BookRepository repository;
    private final BookMapper mapper;

    // converts paginated BookDTOs into a response with HATEOAS links, e.g.: "next", "previous", "self", "last"...
    @Autowired
    PagedResourcesAssembler<BookDTO> assembler;

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

    public PagedModel<EntityModel<BookDTO>> findAll(Pageable pageable) {
        var books = repository.findAll(pageable);
        var pagedDTOs = books.map(book -> {
            BookDTO dto = mapper.toDTO(book);
            addHateoasLinks(dto);
            return dto;
        });

        // adding HATEOAS "self" links for the current page requested by the client.
        Link findAllLink = WebMvcLinkBuilder.linkTo(
            WebMvcLinkBuilder.methodOn(BookController.class)
                .findAll(
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    String.valueOf(pageable.getSort())
                )
        ).withSelfRel();

        return assembler.toModel(pagedDTOs, findAllLink);
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
        bookDTO.add(linkTo(methodOn(BookController.class).findAll(1, 12, "asc")).withRel("findAll").withType("GET"));
        bookDTO.add(linkTo(methodOn(BookController.class).update(bookDTO)).withRel("update").withType("PUT"));
        bookDTO.add(linkTo(methodOn(BookController.class).delete(bookDTO.getId())).withRel("delete").withType("DELETE"));
    }
}