package com.onlinebookstore.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlinebookstore.dto.category.CategoryDto;
import com.onlinebookstore.mapper.impl.BookMapperImpl;
import com.onlinebookstore.mapper.impl.CategoryMapperImpl;
import com.onlinebookstore.model.Category;
import com.onlinebookstore.repository.BookRepository;
import com.onlinebookstore.repository.CategoryRepository;
import com.onlinebookstore.repository.SpecificationProviderManager;
import com.onlinebookstore.repository.spec.BookSpecificationBuilder;
import com.onlinebookstore.service.BookService;
import com.onlinebookstore.service.CategoryService;
import com.onlinebookstore.service.impl.BookServiceImpl;
import com.onlinebookstore.service.impl.CategoryServiceImpl;
import java.util.ArrayList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {CategoryController.class})
@RunWith(SpringRunner.class)
public class CategoryControllerTest {
    @MockBean
    private BookService bookService;

    @Autowired
    private CategoryController categoryController;

    @MockBean
    private CategoryService categoryService;

    @Test
    public void testGetAll3() {
        CategoryRepository categoryRepository = mock(CategoryRepository.class);
        when(categoryRepository.findAll(Mockito.<Pageable>any())).thenReturn(new PageImpl<>(new ArrayList<>()));
        CategoryServiceImpl categoryService = new CategoryServiceImpl(categoryRepository, new CategoryMapperImpl());

        BookRepository bookRepository = mock(BookRepository.class);
        BookMapperImpl bookMapper = new BookMapperImpl();
        assertTrue((new CategoryController(categoryService,
                new BookServiceImpl(bookRepository, bookMapper,
                        new BookSpecificationBuilder(mock(SpecificationProviderManager.class)), mock(CategoryRepository.class))))
                .getAll(null)
                .isEmpty());
        verify(categoryRepository).findAll(Mockito.<Pageable>any());
    }

    @Test
    public void testCreateCategory2() {
        Category category = new Category();
        category.setDeleted(true);
        category.setDescription("The characteristics of someone or something");
        category.setId(1L);
        category.setName("Name");
        CategoryRepository categoryRepository = mock(CategoryRepository.class);
        when(categoryRepository.save(Mockito.<Category>any())).thenReturn(category);
        CategoryServiceImpl categoryService = new CategoryServiceImpl(categoryRepository, new CategoryMapperImpl());

        BookRepository bookRepository = mock(BookRepository.class);
        BookMapperImpl bookMapper = new BookMapperImpl();
        CategoryController categoryController = new CategoryController(categoryService,
                new BookServiceImpl(bookRepository, bookMapper,
                        new BookSpecificationBuilder(mock(SpecificationProviderManager.class)), mock(CategoryRepository.class)));

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setDescription("The characteristics of someone or something");
        categoryDto.setName("Name");
        CategoryDto actualCreateCategoryResult = categoryController.createCategory(categoryDto);
        assertEquals("The characteristics of someone or something", actualCreateCategoryResult.getDescription());
        assertEquals("Name", actualCreateCategoryResult.getName());
        verify(categoryRepository).save(Mockito.<Category>any());
    }

    @Test
    public void testCreateCategory() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        when(this.categoryService.findAll(pageable)).thenReturn(new ArrayList<>());
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName("Name");
        categoryDto.setDescription("The characteristics of someone or something");
        String content = (new ObjectMapper()).writeValueAsString(categoryDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(this.categoryController)
                .build()
                .perform(requestBuilder);
    }

    @Test
    public void testDeleteCategory() throws Exception {
        doNothing().when(this.categoryService).deleteById((Long) any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/{id}", 123L);
        MockMvcBuilders.standaloneSetup(this.categoryController)
                .build()
                .perform(requestBuilder);
    }

    @Test
    public void testDeleteCategory2() throws Exception {
        doNothing().when(this.categoryService).deleteById((Long) any());
        MockHttpServletRequestBuilder deleteResult = MockMvcRequestBuilders.delete("/{id}", 123L);
        deleteResult.contentType("Not all who wander are lost");
        MockMvcBuilders.standaloneSetup(this.categoryController)
                .build()
                .perform(deleteResult);
    }

    @Test
    public void testDeleteCategory3() throws Exception {
        doNothing().when(categoryService).deleteById(Mockito.<Long>any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/categories/{id}", 1L);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(categoryController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void testDeleteCategory4() throws Exception {
        doNothing().when(categoryService).deleteById(Mockito.<Long>any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/categories/{id}", 1L);
        requestBuilder.contentType("https://example.org/example");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(categoryController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void testGetAll2() throws Exception {

        Pageable pageable = PageRequest.of(0, 10);
        when(this.categoryService.findAll(pageable)).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/");
        getResult.contentType("Not all who wander are lost");
        MockMvcBuilders.standaloneSetup(this.categoryController)
                .build()
                .perform(getResult);
    }

    @Test
    public void testGetBooksByCategoryId2() throws Exception {
        when(bookService.findAllByCategoryId(Mockito.<Long>any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/categories/{id}/books", 1L);
        MockMvcBuilders.standaloneSetup(categoryController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    @Test
    public void testGetCategoryById2() throws Exception {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setDescription("The characteristics of someone or something");
        categoryDto.setName("Name");
        when(categoryService.getById(Mockito.<Long>any())).thenReturn(categoryDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/categories/{id}", 1L);
        MockMvcBuilders.standaloneSetup(categoryController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"name\":\"Name\",\"description\":\"The characteristics of someone or something\"}"));
    }

    @Test
    public void testUpdateCategory2() throws Exception {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setDescription("The characteristics of someone or something");
        categoryDto.setName("Name");
        when(categoryService.update(Mockito.<Long>any(), Mockito.<CategoryDto>any())).thenReturn(categoryDto);

        CategoryDto categoryDto2 = new CategoryDto();
        categoryDto2.setDescription("The characteristics of someone or something");
        categoryDto2.setName("Name");
        String content = (new ObjectMapper()).writeValueAsString(categoryDto2);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/categories/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(categoryController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"name\":\"Name\",\"description\":\"The characteristics of someone or something\"}"));
    }
}
