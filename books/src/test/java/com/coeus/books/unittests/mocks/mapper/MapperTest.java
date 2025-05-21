package com.coeus.books.unittests.mocks.mapper;

import com.coeus.books.models.Book;
import com.coeus.books.models.mapper.BookMapper;
import com.coeus.books.unittests.mocks.MockBook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.coeus.books.models.dtos.BookDTO;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MapperTest {

    MockBook mocker;
    BookMapper mapper;

    @BeforeEach
    public void setUp() {
        mocker = new MockBook();
        mapper = Mappers.getMapper(BookMapper.class);
    }

    @Test
    public void parseEntityToDTOTest() {
        BookDTO bookDTO = mapper.toDTO(mocker.mockBookEntity());
        assertEquals(Long.valueOf(0L), bookDTO.getId());
        assertEquals("Book name: 0", bookDTO.getBookName());
        assertEquals("Author name: 0", bookDTO.getAuthorName());
        assertEquals("Publisher name: 0", bookDTO.getPublisherName());
        assertEquals(0, bookDTO.getNumberOfPages());
        assertEquals("Genre: 0", bookDTO.getGenre());
        assertEquals(0, bookDTO.getPrice());
    }

    @Test
    public void parseDTOToEntityTest() {
        Book book = mapper.toEntity(mocker.mockBookDTO());
        assertEquals(Long.valueOf(0L), book.getId());
        assertEquals("Book name: 0", book.getBookName());
        assertEquals("Author name: 0", book.getAuthorName());
        assertEquals("Publisher name: 0", book.getPublisherName());
        assertEquals(0, book.getNumberOfPages());
        assertEquals("Genre: 0", book.getGenre());
        assertEquals(0, book.getPrice());
    }

    @Test
    public void parseEntitiesToDTOSTest() {
        List<BookDTO> bookDTOS = mapper.toDTOS(mocker.mookBookEntities());

        BookDTO bookDTO2 = bookDTOS.get(2);
        assertEquals(Long.valueOf(2L), bookDTO2.getId());
        assertEquals("Book name: 2", bookDTO2.getBookName());
        assertEquals("Author name: 2", bookDTO2.getAuthorName());
        assertEquals("Publisher name: 2", bookDTO2.getPublisherName());
        assertEquals(2, bookDTO2.getNumberOfPages());
        assertEquals("Genre: 2", bookDTO2.getGenre());
        assertEquals(2, bookDTO2.getPrice());

        BookDTO bookDTO6 = bookDTOS.get(6);
        assertEquals(Long.valueOf(6L), bookDTO6.getId());
        assertEquals("Book name: 6", bookDTO6.getBookName());
        assertEquals("Author name: 6", bookDTO6.getAuthorName());
        assertEquals("Publisher name: 6", bookDTO6.getPublisherName());
        assertEquals(6, bookDTO6.getNumberOfPages());
        assertEquals("Genre: 6", bookDTO6.getGenre());
        assertEquals(6, bookDTO6.getPrice());

        BookDTO bookDTO11 = bookDTOS.get(11);
        assertEquals(Long.valueOf(11L), bookDTO11.getId());
        assertEquals("Book name: 11", bookDTO11.getBookName());
        assertEquals("Author name: 11", bookDTO11.getAuthorName());
        assertEquals("Publisher name: 11", bookDTO11.getPublisherName());
        assertEquals(11, bookDTO11.getNumberOfPages());
        assertEquals("Genre: 11", bookDTO11.getGenre());
        assertEquals(11, bookDTO11.getPrice());
    }

    @Test
    public void parseDTOSToEntitiesTest() {
        List<Book> books = mapper.toEntities(mocker.mookBookDTOS());

        Book book2 = books.get(2);
        assertEquals(Long.valueOf(2L), book2.getId());
        assertEquals("Book name: 2", book2.getBookName());
        assertEquals("Author name: 2", book2.getAuthorName());
        assertEquals("Publisher name: 2", book2.getPublisherName());
        assertEquals(2, book2.getNumberOfPages());
        assertEquals("Genre: 2", book2.getGenre());
        assertEquals(2, book2.getPrice());

        Book book6 = books.get(6);
        assertEquals(Long.valueOf(6L), book6.getId());
        assertEquals("Book name: 6", book6.getBookName());
        assertEquals("Author name: 6", book6.getAuthorName());
        assertEquals("Publisher name: 6", book6.getPublisherName());
        assertEquals(6, book6.getNumberOfPages());
        assertEquals("Genre: 6", book6.getGenre());
        assertEquals(6, book6.getPrice());

        Book book11 = books.get(11);
        assertEquals(Long.valueOf(11L), book11.getId());
        assertEquals("Book name: 11", book11.getBookName());
        assertEquals("Author name: 11", book11.getAuthorName());
        assertEquals("Publisher name: 11", book11.getPublisherName());
        assertEquals(11, book11.getNumberOfPages());
        assertEquals("Genre: 11", book11.getGenre());
        assertEquals(11, book11.getPrice());
    }
}