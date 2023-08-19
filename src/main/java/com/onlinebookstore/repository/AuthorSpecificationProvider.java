package com.onlinebookstore.repository;

import com.onlinebookstore.model.Book;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class AuthorSpecificationProvider implements SpecificationProvider<Book> {
    private static final String FILTER_KEY = "author";

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
