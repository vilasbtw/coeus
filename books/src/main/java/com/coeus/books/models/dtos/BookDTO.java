package com.coeus.books.models.dtos;

import org.springframework.hateoas.RepresentationModel;

public class BookDTO extends RepresentationModel<BookDTO> {
    private Long id;
    private String bookName;
    private String authorName;
    private String publisherName;
    private int numberOfPages;
    private String genre;
    private double price;

    public BookDTO(Long id, String bookName, String authorName, String publisherName, int numberOfPages, String genre, double price) {
        this.id = id;
        this.bookName = bookName;
        this.authorName = authorName;
        this.publisherName = publisherName;
        this.numberOfPages = numberOfPages;
        this.genre = genre;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public String getBookName() {
        return bookName;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public int getNumberOfPages() {
        return numberOfPages;
    }

    public String getGenre() {
        return genre;
    }

    public double getPrice() {
        return price;
    }
}