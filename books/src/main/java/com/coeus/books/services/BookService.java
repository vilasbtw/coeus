package com.coeus.books.services;

import com.coeus.books.exceptions.ResourceNotFoundException;
import com.coeus.books.models.dtos.BookDTO;
import com.coeus.books.models.mapper.BookMapper;
import com.coeus.books.repositories.BookRepository;
import com.coeus.books.models.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
        Book entity = mapper.toEntity(bookDTO);
        Book persistedBook = repository.save(entity);
        return mapper.toDTO(persistedBook);
    }

    public BookDTO findById(Long id) {
        Book entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("This resource could not be found."));

        return mapper.toDTO(entity);
    }

    public List<BookDTO> findAll() {
        List<Book> booksList = repository.findAll();
        List<BookDTO> dtosList = new ArrayList<>();

        // repository.findAll().forEach(book -> books.add(mapper.toDTO(book)));

        for (Book book : booksList) {
            dtosList.add(mapper.toDTO(book));
        }
        return dtosList;
    }

    public BookDTO update(BookDTO bookDTO) {
        Book entity = repository.findById(bookDTO.id())
                .orElseThrow(() -> new ResourceNotFoundException("This resource could not be found."));

        entity.setBookName(bookDTO.bookName());
        entity.setAuthorName(bookDTO.authorName());
        entity.setPublisherName(bookDTO.publisherName());
        entity.setNumberOfPages(bookDTO.numberOfPages());
        entity.setGenre(bookDTO.genre());
        entity.setPrice(bookDTO.price());

        repository.save(entity);
        return mapper.toDTO(entity);
    }

    // delete method do not implement DTO pattern, since the method do not return an object
    public void delete(Long id) {
        Book entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("This resource could not be found."));

        repository.delete(entity);
    }
}