package com.onlinebookstore.service;

import com.onlinebookstore.dto.category.CategoryDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    CategoryDto save(CategoryDto requestDto);

    List<CategoryDto> findAll(Pageable pageable);

    CategoryDto getById(Long id);

    CategoryDto update(Long id, CategoryDto requestDto);

    void deleteById(Long id);
}
