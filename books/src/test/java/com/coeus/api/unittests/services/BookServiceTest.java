package com.coeus.api.unittests.services;

import com.coeus.api.exceptions.RequiredObjectIsNullException;
import com.coeus.api.models.Book;
import com.coeus.api.models.dtos.BookDTO;
import com.coeus.api.models.mapper.BookMapper;
import com.coeus.api.repositories.BookRepository;
import com.coeus.api.services.BookService;
import com.coeus.api.unittests.mocks.MockBook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    MockBook input;
    BookService service;
    @Mock
    BookRepository repository;
    BookMapper mapper;

    @Mock
    PagedResourcesAssembler<BookDTO> assembler;

    @BeforeEach
    void setUp() {
        input = new MockBook();
        mapper = Mappers.getMapper(BookMapper.class);
        service = new BookService(repository, mapper, assembler);
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
                        && link.getType().equals("GET")));

        assertTrue(result.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("findAll")
                    && link.getHref().contains("/books")
                    && link.getType().equals("GET")));

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create")
                        && link.getHref().endsWith("/books")
                        && link.getType().equals("POST")));

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update")
                        && link.getHref().endsWith("/books")
                        && link.getType().equals("PUT")));

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete")
                        && link.getHref().endsWith("/books/1")
                        && link.getType().equals("DELETE")));

        assertEquals("Book name: 1", result.getBookName());
        assertEquals("Author name: 1", result.getAuthorName());
        assertEquals("Publisher name: 1", result.getPublisherName());
        assertEquals(1, result.getNumberOfPages());
        assertEquals("Genre: 1", result.getGenre());
        assertEquals(1, result.getPrice());
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
        BookDTO dto = input.mockBookDTO(1);
        Book entity = input.mockBookEntity(1);

        when(repository.save(any(Book.class))).thenReturn(entity);

        BookDTO result = service.create(dto);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("self")
                        && link.getHref().endsWith("/books/1")
                        && link.getType().equals("GET")));

        assertTrue(result.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("findAll")
                    && link.getHref().contains("/books")
                    && link.getType().equals("GET")));

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create")
                        && link.getHref().endsWith("/books")
                        && link.getType().equals("POST")));

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update")
                        && link.getHref().endsWith("/books")
                        && link.getType().equals("PUT")));

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete")
                        && link.getHref().endsWith("/books/1")
                        && link.getType().equals("DELETE")));

        assertEquals("Book name: 1", result.getBookName());
        assertEquals("Author name: 1", result.getAuthorName());
        assertEquals("Publisher name: 1", result.getPublisherName());
        assertEquals(1, result.getNumberOfPages());
        assertEquals("Genre: 1", result.getGenre());
        assertEquals(1, result.getPrice());
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
        BookDTO dto = input.mockBookDTO(1);
        Book entity = input.mockBookEntity(1);

        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(repository.save(any(Book.class))).thenReturn(entity);

        BookDTO result = service.update(dto);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("self")
                        && link.getHref().endsWith("/books/1")
                        && link.getType().equals("GET")));

        assertTrue(result.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("findAll")
                    && link.getHref().contains("/books")
                    && link.getType().equals("GET")));

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create")
                        && link.getHref().endsWith("/books")
                        && link.getType().equals("POST")));

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update")
                        && link.getHref().endsWith("/books")
                        && link.getType().equals("PUT")));

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete")
                        && link.getHref().endsWith("/books/1")
                        && link.getType().equals("DELETE")));

        assertEquals("Book name: 1", result.getBookName());
        assertEquals("Author name: 1", result.getAuthorName());
        assertEquals("Publisher name: 1", result.getPublisherName());
        assertEquals(1, result.getNumberOfPages());
        assertEquals("Genre: 1", result.getGenre());
        assertEquals(1, result.getPrice());
    }
    @Test
    void delete() {
        Book book = input.mockBookEntity(1);
        when(repository.findById(1L)).thenReturn(Optional.of(book));

        service.delete(1L);
    }

    @Test
    void findAll() {
        List<Book> entityList = input.mookBookEntities();
        Page<Book> entityPage = new PageImpl<>(entityList);
        when(repository.findAll(any(Pageable.class))).thenReturn(entityPage);

        when(assembler.toModel(any(Page.class), any(Link.class))).thenAnswer(invocation -> {
            Page<BookDTO> dtoPage = invocation.getArgument(0);
            List<EntityModel<BookDTO>> models = dtoPage.getContent().stream()
                    .map(EntityModel::of)
                    .collect(Collectors.toList());
            PagedModel.PageMetadata metadata = new PagedModel.PageMetadata(
                dtoPage.getSize(),
                dtoPage.getNumber(),
                dtoPage.getTotalElements()
            );
            return PagedModel.of(models, metadata);
        });

        Pageable pageable = PageRequest.of(0, 15);

        PagedModel<EntityModel<BookDTO>> result = service.findAll(pageable);

        assertNotNull(result);
        List<BookDTO> books = result.getContent().stream()
                .map(EntityModel::getContent)
                .collect(Collectors.toList());

        assertNotNull(books);
        assertEquals(15, books.size());

        validateIndividualBook(books.get(1), 1);
        validateIndividualBook(books.get(4), 4);
        validateIndividualBook(books.get(7), 7);
    }

    private void validateIndividualBook(BookDTO book, int i) {
        assertEquals("Book name: " + i, book.getBookName());
        assertEquals("Author name: " + i, book.getAuthorName());
        assertEquals("Publisher name: " + i, book.getPublisherName());
        assertEquals(i, book.getNumberOfPages());
        assertEquals("Genre: " + i, book.getGenre());
        assertEquals(i, book.getPrice());

        assertNotNull(book);
        assertNotNull(book.getId());
        assertNotNull(book.getLinks());

        assertTrue(book.getLink("self").isPresent());
        assertTrue(book.getLink("self").get().getHref().endsWith("/books/" + i));

        assertTrue(book.getLink("findAll").isPresent());
        assertTrue(book.getLink("findAll").get().getHref().contains("/books"));

        assertTrue(book.getLink("create").isPresent());
        assertTrue(book.getLink("create").get().getHref().endsWith("/books"));

        assertTrue(book.getLink("update").isPresent());
        assertTrue(book.getLink("update").get().getHref().endsWith("/books"));

        assertTrue(book.getLink("delete").isPresent());
        assertTrue(book.getLink("delete").get().getHref().endsWith("/books/" + i));
    }

}