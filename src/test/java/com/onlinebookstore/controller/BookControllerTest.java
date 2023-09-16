package com.onlinebookstore.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlinebookstore.dto.book.BookDto;
import com.onlinebookstore.dto.book.CreateBookRequestDto;
import com.onlinebookstore.mapper.impl.BookMapperImpl;
import com.onlinebookstore.model.Book;
import com.onlinebookstore.repository.BookRepository;
import com.onlinebookstore.repository.CategoryRepository;
import com.onlinebookstore.repository.SpecificationProviderManager;
import com.onlinebookstore.repository.spec.BookSpecificationBuilder;
import com.onlinebookstore.service.BookService;
import com.onlinebookstore.service.impl.BookServiceImpl;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {BookController.class})
@RunWith(SpringRunner.class)
public class BookControllerTest {
    @Autowired
    private BookController bookController;

    @MockBean
    private BookService bookService;

    @Test
    public void testFindAll() {
        Pageable pageable = PageRequest.of(0, 10);
        BookService bookService = mock(BookService.class);
        ArrayList<BookDto> bookDtoList = new ArrayList<>();
        when(bookService.findAll(pageable)).thenReturn(bookDtoList);
        List<BookDto> actualFindAllResult = (new BookController(bookService)).findAll(pageable);
        assertSame(bookDtoList, actualFindAllResult);
        assertTrue(actualFindAllResult.isEmpty());
        verify(bookService).findAll(pageable);
    }

    @Test
    public void testFindById() throws Exception {
        BookDto bookDto = new BookDto();
        bookDto.setIsbn("Isbn");
        bookDto.setCategoryIds(new HashSet<>());
        bookDto.setId(123L);
        bookDto.setPrice(BigDecimal.valueOf(42L));
        bookDto.setTitle("Dr");
        bookDto.setAuthor("JaneDoe");
        bookDto.setCoverImage("Cover Image");
        bookDto.setDescription("The characteristics of someone or something");
        when(this.bookService.findById(any())).thenReturn(bookDto);
        MockHttpServletRequestBuilder requestBuilder
                = MockMvcRequestBuilders.get("/books/{id}", 123L);
        MockMvcBuilders.standaloneSetup(this.bookController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"id\":123,\"title\":\"Dr\",\"author\":\"JaneDoe\",\"isbn\""
                                + ":\"Isbn\",\"price\":42,\"description\":\"The characteristics"
                                + " of someone or something\",\"coverImage\":\"Cover Image\""
                                + ",\"categoryIds\":[]}"));
    }

    @Test
    public void testSearch() throws Exception {
        when(bookService.search(Mockito.any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/books/search");
        MockMvcBuilders.standaloneSetup(bookController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    @Test
    public void testSave() {
        Book book = new Book();
        book.setAuthor("JaneDoe");
        book.setCategories(new HashSet<>());
        book.setCoverImage("Cover Image");
        book.setDeleted(true);
        book.setDescription("The characteristics of someone or something");
        book.setId(1L);
        book.setIsbn("Isbn");
        book.setPrice(BigDecimal.valueOf(1L));
        book.setTitle("Dr");
        BookRepository bookRepository = mock(BookRepository.class);
        when(bookRepository.save(Mockito.any())).thenReturn(book);
        CategoryRepository categoryRepository = mock(CategoryRepository.class);
        when(categoryRepository.findByIdIn(Mockito.any())).thenReturn(new HashSet<>());
        BookMapperImpl bookMapper = new BookMapperImpl();
        BookController bookController = new BookController(
                new BookServiceImpl(bookRepository, bookMapper,
                        new BookSpecificationBuilder(
                        mock(SpecificationProviderManager.class)), categoryRepository));

        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setAuthor("JaneDoe");
        requestDto.setCategoryIds(new HashSet<>());
        requestDto.setCoverImage("Cover Image");
        requestDto.setDescription("The characteristics of someone or something");
        requestDto.setIsbn("Isbn");
        BigDecimal price = BigDecimal.valueOf(1L);
        requestDto.setPrice(price);
        requestDto.setTitle("Dr");
        BookDto actualSaveResult = bookController.save(requestDto);
        assertEquals("JaneDoe", actualSaveResult.getAuthor());
        assertEquals("Dr", actualSaveResult.getTitle());
        BigDecimal expectedPrice = BigDecimal.ONE;
        BigDecimal price2 = actualSaveResult.getPrice();
        assertSame(expectedPrice, price2);
        assertEquals("Isbn", actualSaveResult.getIsbn());
        assertTrue(actualSaveResult.getCategoryIds().isEmpty());
        assertEquals("The characteristics of someone or something",
                actualSaveResult.getDescription());
        assertEquals(1L, actualSaveResult.getId().longValue());
        assertEquals("Cover Image", actualSaveResult.getCoverImage());
        assertEquals("1", price2.toString());
        verify(bookRepository).save(Mockito.any());
        verify(categoryRepository).findByIdIn(Mockito.any());
    }

    @Test
    public void testDelete() throws Exception {
        doNothing().when(bookService).deleteById(Mockito.<Long>any());
        MockHttpServletRequestBuilder requestBuilder
                = MockMvcRequestBuilders.delete("/books/{id}", 1L);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(bookController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void testUpdate() throws Exception {
        CreateBookRequestDto createBookRequestDto = new CreateBookRequestDto();
        createBookRequestDto.setAuthor("JaneDoe");
        createBookRequestDto.setCategoryIds(new HashSet<>());
        createBookRequestDto.setCoverImage("Cover Image");
        createBookRequestDto.setDescription("The characteristics of someone or something");
        createBookRequestDto.setIsbn("Isbn");
        createBookRequestDto.setPrice(BigDecimal.valueOf(1L));
        createBookRequestDto.setTitle("Dr");
        String content = (new ObjectMapper()).writeValueAsString(createBookRequestDto);
        MockHttpServletRequestBuilder requestBuilder
                = MockMvcRequestBuilders.post("/books/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(bookController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }
}
