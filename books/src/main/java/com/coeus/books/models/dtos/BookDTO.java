package com.coeus.books.models.dtos;

import lombok.Builder;

// (toBuilder = true) allows increment or decrement attributes in the object BookDTO
@Builder(toBuilder = true)
public class BookDTO {

    private Long id;
    private String bookName;
    private String authorName;
    private String publisherName;
    private int numberOfPages;
    private String genre;
    private double price;

    public BookDTO() {
    }

    public BookDTO(
            Long id,
            String bookName,
            String authorName,
            String publisherName,
            int numberOfPages,
            String genre,
            double price
    ) {}

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
}