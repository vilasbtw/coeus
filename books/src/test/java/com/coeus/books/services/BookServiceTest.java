package com.coeus.books.services;

import com.coeus.books.models.Book;
import com.coeus.books.models.dtos.BookDTO;
import com.coeus.books.models.mapper.BookMapper;
import com.coeus.books.repositories.BookRepository;
import com.coeus.books.unittests.mocks.MockBook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    MockBook input;
    BookService service;
    @Mock
    BookRepository repository;
    BookMapper mapper;

    @BeforeEach
    void setUp() {
        input = new MockBook();
        mapper = Mappers.getMapper(BookMapper.class);
        service = new BookService(repository, mapper);
    }

    @Test
    void findById() {
        Book book = input.mockBookEntity(1);
        when(repository.findById(1L)).thenReturn(Optional.of(book));

        BookDTO result = service.findById(1L);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("self")
                        && link.getHref().endsWith("/books/1")
                        && link.getType().equals("GET")
                )
        );

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("findAll")
                        && link.getHref().endsWith("/books")
                        && link.getType().equals("GET")
                )
        );

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create")
                        && link.getHref().endsWith("/books")
                        && link.getType().equals("POST")
                )
        );

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update")
                        && link.getHref().endsWith("/books")
                        && link.getType().equals("PUT")
                )
        );

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete")
                        && link.getHref().endsWith("/books/1")
                        && link.getType().equals("DELETE")
                )
        );

        assertEquals("Book name: 1", book.getBookName());
        assertEquals("Author name: 1", book.getAuthorName());
        assertEquals("Publisher name: 1", book.getPublisherName());
        assertEquals(1, book.getNumberOfPages());
        assertEquals("Genre: 1", book.getGenre());
        assertEquals(1, book.getPrice());
    }

    @Test
    void create() {
        Book book = input.mockBookEntity(1);
        Book persisted = book;

        BookDTO dto = input.mockBookDTO(1);

        when(repository.save(book)).thenReturn(persisted);

        BookDTO result = service.create(dto);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("self")
                        && link.getHref().endsWith("/books/1")
                        && link.getType().equals("GET")
                )
        );

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("findAll")
                        && link.getHref().endsWith("/books")
                        && link.getType().equals("GET")
                )
        );

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create")
                        && link.getHref().endsWith("/books")
                        && link.getType().equals("POST")
                )
        );

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update")
                        && link.getHref().endsWith("/books")
                        && link.getType().equals("PUT")
                )
        );

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete")
                        && link.getHref().endsWith("/books/1")
                        && link.getType().equals("DELETE")
                )
        );

        assertEquals("Book name: 1", book.getBookName());
        assertEquals("Author name: 1", book.getAuthorName());
        assertEquals("Publisher name: 1", book.getPublisherName());
        assertEquals(1, book.getNumberOfPages());
        assertEquals("Genre: 1", book.getGenre());
        assertEquals(1, book.getPrice());
    }

    @Test
    void update() {
        Book book = input.mockBookEntity(1);
        Book persisted = book;

        BookDTO dto = input.mockBookDTO(1);

        when(repository.findById(1L)).thenReturn(Optional.of(book));
        when(repository.save(book)).thenReturn(persisted);

        BookDTO result = service.update(dto);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("self")
                        && link.getHref().endsWith("/books/1")
                        && link.getType().equals("GET")
                )
        );

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("findAll")
                        && link.getHref().endsWith("/books")
                        && link.getType().equals("GET")
                )
        );

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create")
                        && link.getHref().endsWith("/books")
                        && link.getType().equals("POST")
                )
        );

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update")
                        && link.getHref().endsWith("/books")
                        && link.getType().equals("PUT")
                )
        );

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete")
                        && link.getHref().endsWith("/books/1")
                        && link.getType().equals("DELETE")
                )
        );

        assertEquals("Book name: 1", book.getBookName());
        assertEquals("Author name: 1", book.getAuthorName());
        assertEquals("Publisher name: 1", book.getPublisherName());
        assertEquals(1, book.getNumberOfPages());
        assertEquals("Genre: 1", book.getGenre());
        assertEquals(1, book.getPrice());
    }

    @Test
    void delete() {
        Book book = input.mockBookEntity(1);
        when(repository.findById(1L)).thenReturn(Optional.of(book));

        service.delete(1L);
    }

    @Test
    void findAll() {
    }
}