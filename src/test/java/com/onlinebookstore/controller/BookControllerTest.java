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
import com.onlinebookstore.dto.book.BookSearchParameters;
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
import java.util.Set;
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
    public void testFindAll2() {
        BookRepository bookRepository = mock(BookRepository.class);
        when(bookRepository.findAllWithCategories(Mockito.<Pageable>any())).thenReturn(new ArrayList<>());
        BookMapperImpl bookMapper = new BookMapperImpl();
        assertTrue((new BookController(new BookServiceImpl(bookRepository, bookMapper,
                new BookSpecificationBuilder(mock(SpecificationProviderManager.class)), mock(CategoryRepository.class))))
                .findAll(null)
                .isEmpty());
        verify(bookRepository).findAllWithCategories(Mockito.<Pageable>any());
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
        when(this.bookService.findById((Long) any())).thenReturn(bookDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/books/{id}", 123L);
        MockMvcBuilders.standaloneSetup(this.bookController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":123,\"title\":\"Dr\",\"author\":\"JaneDoe\",\"isbn\":\"Isbn\",\"price\":42,\"description\":\"The characteristics"
                                + " of someone or something\",\"coverImage\":\"Cover Image\",\"categoryIds\":[]}"));
    }

    @Test
    public void testFindById2() throws Exception {
        BookDto bookDto = new BookDto();
        bookDto.setAuthor("JaneDoe");
        bookDto.setCategoryIds(new HashSet<>());
        bookDto.setCoverImage("Cover Image");
        bookDto.setDescription("The characteristics of someone or something");
        bookDto.setId(1L);
        bookDto.setIsbn("Isbn");
        bookDto.setPrice(BigDecimal.valueOf(1L));
        bookDto.setTitle("Dr");
        when(bookService.findById(Mockito.<Long>any())).thenReturn(bookDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/books/{id}", 1L);
        MockMvcBuilders.standaloneSetup(bookController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":1,\"title\":\"Dr\",\"author\":\"JaneDoe\",\"isbn\":\"Isbn\",\"price\":1,\"description\":\"The characteristics of"
                                + " someone or something\",\"coverImage\":\"Cover Image\",\"categoryIds\":[]}"));
    }

    @Test
    public void testSearch() throws Exception {
        when(this.bookService.search((com.onlinebookstore.dto.book.BookSearchParameters) any())).thenReturn(null);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/books/search");
        MockMvcBuilders.standaloneSetup(this.bookController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testSearch2() throws Exception {
        when(bookService.search(Mockito.<BookSearchParameters>any())).thenReturn(new ArrayList<>());
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
        book.setIsbn("Isbn");
        book.setId(123L);
        book.setDeleted(true);
        book.setPrice(BigDecimal.valueOf(42L));
        book.setTitle("Dr");
        book.setAuthor("JaneDoe");
        book.setCoverImage("Cover Image");
        book.setCategories(new HashSet<>());
        book.setDescription("The characteristics of someone or something");
        BookRepository bookRepository = mock(BookRepository.class);
        when(bookRepository.save((Book) any())).thenReturn(book);
        CategoryRepository categoryRepository = mock(CategoryRepository.class);
        when(categoryRepository.findByIdIn((Set<Long>) any())).thenReturn(new HashSet<>());
        BookMapperImpl bookMapper = new BookMapperImpl();
        BookController bookController = new BookController(new BookServiceImpl(bookRepository, bookMapper,
                new BookSpecificationBuilder((SpecificationProviderManager<Book>) mock(SpecificationProviderManager.class)),
                categoryRepository));

        CreateBookRequestDto createBookRequestDto = new CreateBookRequestDto();
        createBookRequestDto.setIsbn("Isbn");
        createBookRequestDto.setCategoryIds(new HashSet<>());
        BigDecimal valueOfResult = BigDecimal.valueOf(42L);
        createBookRequestDto.setPrice(valueOfResult);
        createBookRequestDto.setTitle("Dr");
        createBookRequestDto.setAuthor("JaneDoe");
        createBookRequestDto.setCoverImage("Cover Image");
        createBookRequestDto.setDescription("The characteristics of someone or something");
        BookDto actualSaveResult = bookController.save(createBookRequestDto);
        assertEquals("JaneDoe", actualSaveResult.getAuthor());
        assertEquals("Dr", actualSaveResult.getTitle());
        BigDecimal price = actualSaveResult.getPrice();
        assertEquals(valueOfResult, price);
        assertEquals("Isbn", actualSaveResult.getIsbn());
        assertTrue(actualSaveResult.getCategoryIds().isEmpty());
        assertEquals("The characteristics of someone or something", actualSaveResult.getDescription());
        assertEquals(123L, actualSaveResult.getId().longValue());
        assertEquals("Cover Image", actualSaveResult.getCoverImage());
        assertEquals("42", price.toString());
        verify(bookRepository).save((Book) any());
        verify(categoryRepository).findByIdIn((Set<Long>) any());
    }

    @Test
    public void testSave2() {
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
        when(bookRepository.save(Mockito.<Book>any())).thenReturn(book);
        CategoryRepository categoryRepository = mock(CategoryRepository.class);
        when(categoryRepository.findByIdIn(Mockito.<Set<Long>>any())).thenReturn(new HashSet<>());
        BookMapperImpl bookMapper = new BookMapperImpl();
        BookController bookController = new BookController(new BookServiceImpl(bookRepository, bookMapper,
                new BookSpecificationBuilder(mock(SpecificationProviderManager.class)), categoryRepository));

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
        BigDecimal expectedPrice = price.ONE;
        BigDecimal price2 = actualSaveResult.getPrice();
        assertSame(expectedPrice, price2);
        assertEquals("Isbn", actualSaveResult.getIsbn());
        assertTrue(actualSaveResult.getCategoryIds().isEmpty());
        assertEquals("The characteristics of someone or something", actualSaveResult.getDescription());
        assertEquals(1L, actualSaveResult.getId().longValue());
        assertEquals("Cover Image", actualSaveResult.getCoverImage());
        assertEquals("1", price2.toString());
        verify(bookRepository).save(Mockito.<Book>any());
        verify(categoryRepository).findByIdIn(Mockito.<Set<Long>>any());
    }

    @Test
    public void testUpdate2() throws Exception {
        BookDto bookDto = new BookDto();
        bookDto.setIsbn("Isbn");
        bookDto.setCategoryIds(new HashSet<>());
        bookDto.setId(123L);
        bookDto.setPrice(BigDecimal.valueOf(42L));
        bookDto.setTitle("Dr");
        bookDto.setAuthor("JaneDoe");
        bookDto.setCoverImage("Cover Image");
        bookDto.setDescription("The characteristics of someone or something");
        when(this.bookService.update((Long) any(), (CreateBookRequestDto) any())).thenReturn(bookDto);

        CreateBookRequestDto createBookRequestDto = new CreateBookRequestDto();
        createBookRequestDto.setIsbn("999999999999");
        createBookRequestDto.setCategoryIds(new HashSet<>());
        createBookRequestDto.setPrice(BigDecimal.valueOf(42L));
        createBookRequestDto.setTitle("Dr");
        createBookRequestDto.setAuthor("JaneDoe");
        createBookRequestDto.setCoverImage("Cover Image");
        createBookRequestDto.setDescription("The characteristics of someone or something");
        String content = (new ObjectMapper()).writeValueAsString(createBookRequestDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/books/{id}", 123L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(this.bookController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":123,\"title\":\"Dr\",\"author\":\"JaneDoe\",\"isbn\":\"Isbn\",\"price\":42,\"description\":\"The characteristics"
                                + " of someone or something\",\"coverImage\":\"Cover Image\",\"categoryIds\":[]}"));
    }

    @Test
    public void testUpdate4() throws Exception {
        BookDto bookDto = new BookDto();
        bookDto.setAuthor("JaneDoe");
        bookDto.setCategoryIds(new HashSet<>());
        bookDto.setCoverImage("Cover Image");
        bookDto.setDescription("The characteristics of someone or something");
        bookDto.setId(1L);
        bookDto.setIsbn("Isbn");
        bookDto.setPrice(BigDecimal.valueOf(1L));
        bookDto.setTitle("Dr");
        when(bookService.update(Mockito.<Long>any(), Mockito.<CreateBookRequestDto>any())).thenReturn(bookDto);

        CreateBookRequestDto createBookRequestDto = new CreateBookRequestDto();
        createBookRequestDto.setAuthor("JaneDoe");
        createBookRequestDto.setCategoryIds(new HashSet<>());
        createBookRequestDto.setCoverImage("Cover Image");
        createBookRequestDto.setDescription("The characteristics of someone or something");
        createBookRequestDto.setIsbn("999999999999");
        createBookRequestDto.setPrice(BigDecimal.valueOf(1L));
        createBookRequestDto.setTitle("Dr");
        String content = (new ObjectMapper()).writeValueAsString(createBookRequestDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/books/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(bookController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":1,\"title\":\"Dr\",\"author\":\"JaneDoe\",\"isbn\":\"Isbn\",\"price\":1,\"description\":\"The characteristics of"
                                + " someone or something\",\"coverImage\":\"Cover Image\",\"categoryIds\":[]}"));
    }

    @Test
    public void testDelete() throws Exception {
        doNothing().when(this.bookService).deleteById((Long) any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/books/{id}", 123L);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.bookController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void testDelete2() throws Exception {
        doNothing().when(bookService).deleteById(Mockito.<Long>any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/books/{id}", 1L);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(bookController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void testDelete3() throws Exception {
        doNothing().when(bookService).deleteById(Mockito.<Long>any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/books/{id}", 1L);
        requestBuilder.contentType("https://example.org/example");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(bookController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void testUpdate() throws Exception {
        CreateBookRequestDto createBookRequestDto = new CreateBookRequestDto();
        createBookRequestDto.setIsbn("Isbn");
        createBookRequestDto.setCategoryIds(new HashSet<>());
        createBookRequestDto.setPrice(BigDecimal.valueOf(42L));
        createBookRequestDto.setTitle("Dr");
        createBookRequestDto.setAuthor("JaneDoe");
        createBookRequestDto.setCoverImage("Cover Image");
        createBookRequestDto.setDescription("The characteristics of someone or something");
        String content = (new ObjectMapper()).writeValueAsString(createBookRequestDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/books/{id}", 123L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.bookController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }

    @Test
    public void testUpdate3() throws Exception {
        CreateBookRequestDto createBookRequestDto = new CreateBookRequestDto();
        createBookRequestDto.setAuthor("JaneDoe");
        createBookRequestDto.setCategoryIds(new HashSet<>());
        createBookRequestDto.setCoverImage("Cover Image");
        createBookRequestDto.setDescription("The characteristics of someone or something");
        createBookRequestDto.setIsbn("Isbn");
        createBookRequestDto.setPrice(BigDecimal.valueOf(1L));
        createBookRequestDto.setTitle("Dr");
        String content = (new ObjectMapper()).writeValueAsString(createBookRequestDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/books/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(bookController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }
}

