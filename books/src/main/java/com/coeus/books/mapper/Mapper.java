package com.coeus.books.mapper;

import com.coeus.books.dto.BookDTO;
import com.coeus.books.models.Book;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class Mapper implements Function<Book, BookDTO> {

    @Override
    public BookDTO apply(Book book) {
        return new BookDTO(
                book.getId(),
                book.getBookName(),
                book.getAuthorName(),
                book.getPublisherName(),
                book.getNumberOfPages(),
                book.getGenre(),
                book.getPrice()
        );
    }
}