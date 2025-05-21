package com.coeus.books.unittests.mocks;

import com.coeus.books.models.Book;
import com.coeus.books.models.dtos.BookDTO;

import java.util.ArrayList;
import java.util.List;

public class MockBook {

    public Book mockBookEntity() {
        return mockBookEntity(0);
    }

    public Book mockBookEntity(Integer number) {
        Book book = new Book();
        book.setId(number.longValue());
        book.setBookName("Book name: " + number);
        book.setAuthorName("Author name: " + number);
        book.setPublisherName("Publisher name: " + number);
        book.setNumberOfPages(number);
        book.setGenre("Genre: " + number);
        book.setPrice(number);
        return book;
    }

    public List<Book> mookBookEntities() {
        List<Book> books = new ArrayList<>();

        for (int i = 0; i < 15; i++) {
            books.add(mockBookEntity(i));
        }
        return books;
    }

    public BookDTO mockBookDTO() {
        return mockBookDTO(0);
    }

public BookDTO mockBookDTO(Integer number) {
    return new BookDTO(
        number.longValue(),
        "Book name: " + number,
        "Author name: " + number,
        "Publisher name: " + number,
        number,
        "Genre: " + number,
        number.doubleValue()
    );
}


    public List<BookDTO> mookBookDTOS() {
        List<BookDTO> booksDTO = new ArrayList<>();

        for (int i = 0; i < 15; i++) {
            booksDTO.add(mockBookDTO(i));
        }
        return booksDTO;
    }
}