package com.onlinebookstore.service.impl;

import com.onlinebookstore.dto.category.CategoryDto;
import com.onlinebookstore.exception.EntityNotFoundException;
import com.onlinebookstore.mapper.CategoryMapper;
import com.onlinebookstore.model.Category;
import com.onlinebookstore.repository.CategoryRepository;
import com.onlinebookstore.service.CategoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryDto save(CategoryDto requestDto) {
        Category category = categoryMapper.toEntity(requestDto);
        return categoryMapper.toDto(categoryRepository.save(category));
    }

    @Override
    public List<CategoryDto> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable).stream()
                .map(categoryMapper::toDto)
                .toList();
    }

    @Override
    public CategoryDto getById(Long id) {
        return categoryMapper.toDto(findCategoryById(id));
    }

    @Override
    public CategoryDto update(Long id, CategoryDto categoryDto) {
        Category categoryById = findCategoryById(id);
        categoryById.setName(categoryDto.getName());
        categoryById.setDescription(categoryDto.getDescription());
        return categoryMapper.toDto(categoryRepository.save(categoryById));
    }

    @Override
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }

    private Category findCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find category by id: " + id)
        );
    }
}
