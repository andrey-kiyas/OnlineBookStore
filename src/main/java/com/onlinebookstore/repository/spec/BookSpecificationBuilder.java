package com.onlinebookstore.repository.spec;

import com.onlinebookstore.dto.book.BookSearchParameters;
import com.onlinebookstore.model.Book;
import com.onlinebookstore.repository.SpecificationBuilder;
import com.onlinebookstore.repository.SpecificationProviderManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {
    private final SpecificationProviderManager<Book> bookSpecificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParameters bookSearchParameters) {
        Specification<Book> spec = Specification.where(null);
        if (bookSearchParameters.title() != null && bookSearchParameters.title().length > 0) {
            spec = spec.and(bookSpecificationProviderManager
                    .getSpecificationProvider("title")
                    .getSpecification(bookSearchParameters.title()));
        }
        if (bookSearchParameters.author() != null && bookSearchParameters.author().length > 0) {
            spec = spec.and(bookSpecificationProviderManager
                    .getSpecificationProvider("author")
                    .getSpecification(bookSearchParameters.author()));
        }
        if (bookSearchParameters.isbn() != null && bookSearchParameters.isbn().length > 0) {
            spec = spec.and(bookSpecificationProviderManager
                    .getSpecificationProvider("isbn")
                    .getSpecification(bookSearchParameters.isbn()));
        }
        if (bookSearchParameters.price() != null && bookSearchParameters.price().length > 0) {
            spec = spec.and(bookSpecificationProviderManager
                    .getSpecificationProvider("price")
                    .getSpecification(bookSearchParameters.price()));
        }
        if (bookSearchParameters.description() != null && bookSearchParameters.description()
                .length > 0) {
            spec = spec.and(bookSpecificationProviderManager
                    .getSpecificationProvider("description")
                    .getSpecification(bookSearchParameters.description()));
        }
        return spec;
    }
}
