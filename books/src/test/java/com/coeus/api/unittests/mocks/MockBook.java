package com.coeus.api.unittests.mocks;

import com.coeus.api.models.Book;
import com.coeus.api.models.dtos.BookDTO;

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
        book.setStock(number);
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
        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(number.longValue());
        bookDTO.setBookName("Book name: " + number);
        bookDTO.setAuthorName("Author name: " + number);
        bookDTO.setPublisherName("Publisher name: " + number);
        bookDTO.setNumberOfPages(number);
        bookDTO.setGenre("Genre: " + number);
        bookDTO.setPrice(number);
        bookDTO.setStock(number);
        return bookDTO;
    }

    public List<BookDTO> mookBookDTOS() {
        List<BookDTO> booksDTO = new ArrayList<>();

        for (int i = 0; i < 15; i++) {
            booksDTO.add(mockBookDTO(i));
        }
        return booksDTO;
    }
}