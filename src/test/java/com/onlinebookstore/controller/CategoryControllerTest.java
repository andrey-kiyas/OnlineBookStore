package com.onlinebookstore.controller;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlinebookstore.dto.category.CategoryDto;
import com.onlinebookstore.service.BookService;
import com.onlinebookstore.service.CategoryService;
import java.util.ArrayList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
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
        doNothing().when(this.categoryService).deleteById(any());
        MockHttpServletRequestBuilder requestBuilder
                = MockMvcRequestBuilders.delete("/{id}", 123L);
        MockMvcBuilders.standaloneSetup(this.categoryController)
                .build()
                .perform(requestBuilder);
    }

    @Test
    public void testGetAll() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        when(this.categoryService.findAll(pageable)).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/");
        getResult.contentType("Not all who wander are lost");
        MockMvcBuilders.standaloneSetup(this.categoryController)
                .build()
                .perform(getResult);
    }

    @Test
    public void testGetBooksByCategoryId() throws Exception {
        when(bookService.findAllByCategoryId(Mockito.<Long>any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder
                = MockMvcRequestBuilders.get("/categories/{id}/books", 1L);
        MockMvcBuilders.standaloneSetup(categoryController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    @Test
    public void testGetCategoryById() throws Exception {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setDescription("The characteristics of someone or something");
        categoryDto.setName("Name");
        when(categoryService.getById(Mockito.<Long>any())).thenReturn(categoryDto);
        MockHttpServletRequestBuilder requestBuilder
                = MockMvcRequestBuilders.get("/categories/{id}", 1L);
        MockMvcBuilders.standaloneSetup(categoryController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"name\":\"Name\",\"description\":\"The characteristics"
                                + " of someone or something\"}"));
    }

    @Test
    public void testUpdateCategory() throws Exception {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setDescription("The characteristics of someone or something");
        categoryDto.setName("Name");
        when(categoryService.update(Mockito.<Long>any(), Mockito.any()))
                .thenReturn(categoryDto);

        CategoryDto categoryDto2 = new CategoryDto();
        categoryDto2.setDescription("The characteristics of someone or something");
        categoryDto2.setName("Name");
        String content = (new ObjectMapper()).writeValueAsString(categoryDto2);
        MockHttpServletRequestBuilder requestBuilder
                = MockMvcRequestBuilders.post("/categories/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(categoryController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"name\":\"Name\",\"description\":\"The characteristics"
                                + " of someone or something\"}"));
    }
}
