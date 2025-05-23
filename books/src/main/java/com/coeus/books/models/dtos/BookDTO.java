package com.coeus.books.models.dtos;

import org.springframework.hateoas.RepresentationModel;

import java.util.Objects;

public class BookDTO extends RepresentationModel<BookDTO> {
    private Long id;
    private String bookName;
    private String authorName;
    private String publisherName;
    private int numberOfPages;
    private String genre;
    private double price;

    public BookDTO() {}

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
        if (!(o instanceof BookDTO bookDTO)) return false;
        if (!super.equals(o)) return false;
        return numberOfPages == bookDTO.numberOfPages && Double.compare(price, bookDTO.price) == 0 && Objects.equals(id, bookDTO.id) && Objects.equals(bookName, bookDTO.bookName) && Objects.equals(authorName, bookDTO.authorName) && Objects.equals(publisherName, bookDTO.publisherName) && Objects.equals(genre, bookDTO.genre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, bookName, authorName, publisherName, numberOfPages, genre, price);
    }
}