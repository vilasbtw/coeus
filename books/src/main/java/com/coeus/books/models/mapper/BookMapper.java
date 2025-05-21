package com.coeus.books.models.mapper;

import com.coeus.books.models.Book;
import com.coeus.books.models.dtos.BookDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

// (componentModel = "spring") allows Dependency Injection and to autowire our Mapper in other classes
// unmappedTargetPolicy ignores the warnings about unmapped fields
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BookMapper {
    Book toEntity(BookDTO bookDTO);
    BookDTO toDTO(Book book);
    List<Book> toEntities(List<BookDTO> bookDTOS);
    List<BookDTO> toDTOS(List<Book> books);
}