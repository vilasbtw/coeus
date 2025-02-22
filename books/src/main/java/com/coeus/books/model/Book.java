package com.coeus.books.model;

import jakarta.persistence.*;

import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "book_name", nullable = false)
    private String bookName;

    @Column(name = "author_name", nullable = false)
    private String authorName;

    @Column(name = "publisher_name", nullable = false)
    private String publisherName;

    @Column(name = "number_of_pages", nullable = false)
    private int numberOfPages;

    private String genre;

    private double price;

    public Book() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    public int getNumberOfPages() {
        return numberOfPages;
    }

    public void setNumberOfPages(int numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Book book)) return false;
        return id == book.id && numberOfPages == book.numberOfPages && Double.compare(price, book.price) == 0 && Objects.equals(bookName, book.bookName) && Objects.equals(authorName, book.authorName) && Objects.equals(publisherName, book.publisherName) && Objects.equals(genre, book.genre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, bookName, authorName, publisherName, numberOfPages, genre, price);
    }
}