package com.coeus.books.unittests.mocks.mapper;

import com.coeus.books.models.mapper.BookMapper;
import com.coeus.books.unittests.mocks.MockBook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.coeus.books.models.dtos.BookDTO;
import org.mapstruct.factory.Mappers;

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
}