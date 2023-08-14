package com.onlinebookstore.repository.book.spec;

import com.onlinebookstore.model.Book;
import com.onlinebookstore.repository.SpecificationProvider;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class DescriptionSpecificationProvider implements SpecificationProvider<Book> {
    private static final String FILTER_KEY = "description";

    @Override
    public String getKey() {
        return FILTER_KEY;
    }

    public Specification<Book> getSpecification(String[] params) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.like(root.get(FILTER_KEY),
                    "%" + params[0] + "%");
            return criteriaBuilder.and(predicate);
        };
    }
}
