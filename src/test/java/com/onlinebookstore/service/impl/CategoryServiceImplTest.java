package com.onlinebookstore.service.impl;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.onlinebookstore.dto.category.CategoryDto;
import com.onlinebookstore.exception.EntityNotFoundException;
import com.onlinebookstore.mapper.CategoryMapper;
import com.onlinebookstore.model.Category;
import com.onlinebookstore.repository.CategoryRepository;
import java.util.ArrayList;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration(classes = {CategoryServiceImpl.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class CategoryServiceImplTest {
    @MockBean
    private CategoryMapper categoryMapper;

    @MockBean
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryServiceImpl categoryServiceImpl;

    @Test
    public void testSave() {
        Category category = new Category();
        category.setDeleted(true);
        category.setDescription("The characteristics of someone or something");
        category.setId(1L);
        category.setName("Name");
        when(categoryRepository.save(Mockito.any())).thenReturn(category);

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setDescription("The characteristics of someone or something");
        categoryDto.setName("Name");

        Category category2 = new Category();
        category2.setDeleted(true);
        category2.setDescription("The characteristics of someone or something");
        category2.setId(1L);
        category2.setName("Name");
        when(categoryMapper.toDto(Mockito.any())).thenReturn(categoryDto);
        when(categoryMapper.toEntity(Mockito.any())).thenReturn(category2);

        CategoryDto requestDto = new CategoryDto();
        requestDto.setDescription("The characteristics of someone or something");
        requestDto.setName("Name");
        assertSame(categoryDto, categoryServiceImpl.save(requestDto));
        verify(categoryRepository).save(Mockito.any());
        verify(categoryMapper).toDto(Mockito.any());
        verify(categoryMapper).toEntity(Mockito.any());
    }

    @Test
    public void testFindAll() {
        when(categoryRepository.findAll(Mockito.<Pageable>any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        assertTrue(categoryServiceImpl.findAll(null).isEmpty());
        verify(categoryRepository).findAll(Mockito.<Pageable>any());
    }

    @Test
    public void testGetById() {
        Category category = new Category();
        category.setDeleted(true);
        category.setDescription("The characteristics of someone or something");
        category.setId(1L);
        category.setName("Name");
        Optional<Category> ofResult = Optional.of(category);
        when(categoryRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setDescription("The characteristics of someone or something");
        categoryDto.setName("Name");
        when(categoryMapper.toDto(Mockito.any())).thenReturn(categoryDto);
        assertSame(categoryDto, categoryServiceImpl.getById(1L));
        verify(categoryRepository).findById(Mockito.<Long>any());
        verify(categoryMapper).toDto(Mockito.any());
    }

    @Test
    public void testUpdate() {
        Category category = new Category();
        category.setDeleted(true);
        category.setDescription("The characteristics of someone or something");
        category.setId(1L);
        category.setName("Name");

        Category category2 = new Category();
        category2.setDeleted(true);
        category2.setDescription("The characteristics of someone or something");
        category2.setId(1L);
        category2.setName("Name");
        Optional<Category> ofResult = Optional.of(category);
        when(categoryRepository.save(Mockito.any())).thenReturn(category2);
        when(categoryRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        when(categoryMapper.toDto(Mockito.any()))
                .thenThrow(new EntityNotFoundException("An error occurred"));

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setDescription("The characteristics of someone or something");
        categoryDto.setName("Name");
        assertThrows(EntityNotFoundException.class,
                () -> categoryServiceImpl.update(1L, categoryDto)
        );
        verify(categoryRepository).save(Mockito.any());
        verify(categoryRepository).findById(Mockito.<Long>any());
        verify(categoryMapper).toDto(Mockito.any());
    }

    @Test
    public void testDeleteById() {
        doNothing().when(categoryRepository).deleteById(Mockito.<Long>any());
        categoryServiceImpl.deleteById(1L);
        verify(categoryRepository).deleteById(Mockito.<Long>any());
    }
}
