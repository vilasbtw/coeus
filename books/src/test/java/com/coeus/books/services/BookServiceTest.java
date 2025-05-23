package com.coeus.books.services;

import com.coeus.books.exceptions.RequiredObjectIsNullException;
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

import java.util.List;
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
    void testCreateWithNullBook() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class,
                () -> {
                    service.create(null);
                });

        String expectedMessage = "It is not allowed to persist a null object!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
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
    void testUpdateWithNullBook() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class,
                () -> {
                    service.update(null);
                });

        String expectedMessage = "It is not allowed to persist a null object!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
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
        List<Book> list = input.mookBookEntities();
        when(repository.findAll()).thenReturn(list);
        List<BookDTO> dtos = service.findAll();

        assertNotNull(dtos);
        assertEquals(15, dtos.size());

        BookDTO bookOne = dtos.get(1);

        assertTrue(bookOne.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("self")
                        && link.getHref().endsWith("/books/1")
                        && link.getType().equals("GET")
                )
        );

        assertTrue(bookOne.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("findAll")
                        && link.getHref().endsWith("/books")
                        && link.getType().equals("GET")
                )
        );

        assertTrue(bookOne.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create")
                        && link.getHref().endsWith("/books")
                        && link.getType().equals("POST")
                )
        );

        assertTrue(bookOne.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update")
                        && link.getHref().endsWith("/books")
                        && link.getType().equals("PUT")
                )
        );

        assertTrue(bookOne.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete")
                        && link.getHref().endsWith("/books/1")
                        && link.getType().equals("DELETE")
                )
        );

        assertEquals("Book name: 1", bookOne.getBookName());
        assertEquals("Author name: 1", bookOne.getAuthorName());
        assertEquals("Publisher name: 1", bookOne.getPublisherName());
        assertEquals(1, bookOne.getNumberOfPages());
        assertEquals("Genre: 1", bookOne.getGenre());
        assertEquals(1, bookOne.getPrice());

        BookDTO bookSeven = dtos.get(7);

        assertTrue(bookSeven.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("self")
                        && link.getHref().endsWith("/books/7")
                        && link.getType().equals("GET")
                )
        );

        assertTrue(bookSeven.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("findAll")
                        && link.getHref().endsWith("/books")
                        && link.getType().equals("GET")
                )
        );

        assertTrue(bookSeven.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create")
                        && link.getHref().endsWith("/books")
                        && link.getType().equals("POST")
                )
        );

        assertTrue(bookSeven.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update")
                        && link.getHref().endsWith("/books")
                        && link.getType().equals("PUT")
                )
        );

        assertTrue(bookSeven.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete")
                        && link.getHref().endsWith("/books/7")
                        && link.getType().equals("DELETE")
                )
        );

        assertEquals("Book name: 7", bookSeven.getBookName());
        assertEquals("Author name: 7", bookSeven.getAuthorName());
        assertEquals("Publisher name: 7", bookSeven.getPublisherName());
        assertEquals(7, bookSeven.getNumberOfPages());
        assertEquals("Genre: 7", bookSeven.getGenre());
        assertEquals(7, bookSeven.getPrice());

        BookDTO bookThirteen = dtos.get(13);

        assertTrue(bookThirteen.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("self")
                        && link.getHref().endsWith("/books/13")
                        && link.getType().equals("GET")
                )
        );

        assertTrue(bookThirteen.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("findAll")
                        && link.getHref().endsWith("/books")
                        && link.getType().equals("GET")
                )
        );

        assertTrue(bookThirteen.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create")
                        && link.getHref().endsWith("/books")
                        && link.getType().equals("POST")
                )
        );

        assertTrue(bookThirteen.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update")
                        && link.getHref().endsWith("/books")
                        && link.getType().equals("PUT")
                )
        );

        assertTrue(bookThirteen.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete")
                        && link.getHref().endsWith("/books/13")
                        && link.getType().equals("DELETE")
                )
        );

        assertEquals("Book name: 13", bookThirteen.getBookName());
        assertEquals("Author name: 13", bookThirteen.getAuthorName());
        assertEquals("Publisher name: 13", bookThirteen.getPublisherName());
        assertEquals(13, bookThirteen.getNumberOfPages());
        assertEquals("Genre: 13", bookThirteen.getGenre());
        assertEquals(13, bookThirteen.getPrice());
    }
}