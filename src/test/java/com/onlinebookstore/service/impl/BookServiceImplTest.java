package com.onlinebookstore.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.onlinebookstore.dto.book.BookDto;
import com.onlinebookstore.dto.book.BookSearchParameters;
import com.onlinebookstore.dto.book.CreateBookRequestDto;
import com.onlinebookstore.exception.EntityNotFoundException;
import com.onlinebookstore.mapper.BookMapper;
import com.onlinebookstore.model.Book;
import com.onlinebookstore.repository.BookRepository;
import com.onlinebookstore.repository.CategoryRepository;
import com.onlinebookstore.repository.spec.BookSpecificationBuilder;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@ContextConfiguration(classes = {BookServiceImpl.class})
@RunWith(SpringRunner.class)
public class BookServiceImplTest {
    @MockBean
    private BookMapper bookMapper;

    @MockBean
    private BookRepository bookRepository;

    @Autowired
    private BookServiceImpl bookServiceImpl;

    @MockBean
    private BookSpecificationBuilder bookSpecificationBuilder;

    @MockBean
    private CategoryRepository categoryRepository;

    @Test
    public void testSave() {
        when(this.categoryRepository.findByIdIn(any())).thenReturn(new HashSet<>());

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
        when(this.bookRepository.save(any())).thenReturn(book);

        Book book1 = new Book();
        book1.setIsbn("Isbn");
        book1.setId(123L);
        book1.setDeleted(true);
        book1.setPrice(BigDecimal.valueOf(42L));
        book1.setTitle("Dr");
        book1.setAuthor("JaneDoe");
        book1.setCoverImage("Cover Image");
        book1.setCategories(new HashSet<>());
        book1.setDescription("The characteristics of someone or something");

        BookDto bookDto = new BookDto();
        bookDto.setIsbn("Isbn");
        bookDto.setCategoryIds(new HashSet<>());
        bookDto.setId(123L);
        bookDto.setPrice(BigDecimal.valueOf(42L));
        bookDto.setTitle("Dr");
        bookDto.setAuthor("JaneDoe");
        bookDto.setCoverImage("Cover Image");
        bookDto.setDescription("The characteristics of someone or something");
        when(this.bookMapper.toDto(any())).thenReturn(bookDto);
        when(this.bookMapper.toModel(any())).thenReturn(book1);

        CreateBookRequestDto createBookRequestDto = new CreateBookRequestDto();
        createBookRequestDto.setIsbn("Isbn");
        createBookRequestDto.setCategoryIds(new HashSet<>());
        createBookRequestDto.setPrice(BigDecimal.valueOf(42L));
        createBookRequestDto.setTitle("Dr");
        createBookRequestDto.setAuthor("JaneDoe");
        createBookRequestDto.setCoverImage("Cover Image");
        createBookRequestDto.setDescription("The characteristics of someone or something");
        BookDto actualSaveResult = this.bookServiceImpl.save(createBookRequestDto);
        assertSame(bookDto, actualSaveResult);
        assertEquals("42", actualSaveResult.getPrice().toString());
        verify(this.categoryRepository).findByIdIn(any());
        verify(this.bookRepository).save(any());
        verify(this.bookMapper).toDto(any());
        verify(this.bookMapper).toModel(any());
    }

    @Test
    public void testFindAll() {
        when(bookRepository.findAllWithCategories(Mockito.any()))
                .thenReturn(new ArrayList<>());
        assertTrue(bookServiceImpl.findAll(null).isEmpty());
        verify(bookRepository).findAllWithCategories(Mockito.any());
    }

    @Test
    public void testFindById() {
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
        Optional<Book> ofResult = Optional.of(book);
        when(this.bookRepository.findBookById(any())).thenReturn(ofResult);

        BookDto bookDto = new BookDto();
        bookDto.setIsbn("Isbn");
        bookDto.setCategoryIds(new HashSet<>());
        bookDto.setId(123L);
        bookDto.setPrice(BigDecimal.valueOf(42L));
        bookDto.setTitle("Dr");
        bookDto.setAuthor("JaneDoe");
        bookDto.setCoverImage("Cover Image");
        bookDto.setDescription("The characteristics of someone or something");
        when(this.bookMapper.toDto(any())).thenReturn(bookDto);
        BookDto actualFindByIdResult = this.bookServiceImpl.findById(123L);
        assertSame(bookDto, actualFindByIdResult);
        assertEquals("42", actualFindByIdResult.getPrice().toString());
        verify(this.bookRepository).findBookById(any());
        verify(this.bookMapper).toDto(any());
    }

    @Test
    public void testDeleteById() {
        doNothing().when(this.bookRepository).deleteById(any());
        this.bookServiceImpl.deleteById(123L);
        verify(this.bookRepository).deleteById(any());
    }

    @Test
    public void testSearch() {
        when(bookSpecificationBuilder.build(Mockito.any()))
                .thenThrow(new EntityNotFoundException("An error occurred"));
        assertThrows(EntityNotFoundException.class,
                () -> bookServiceImpl.search(new BookSearchParameters(
                        new String[]{"Dr"},
                        new String[]{"JaneDoe"},
                        new String[]{"Isbn"},
                        new String[]{"Price"},
                        new String[]{"The characteristics of someone or something"},
                        new String[]{"Categories"})));
        verify(bookSpecificationBuilder).build(Mockito.any());
    }

    @Test
    public void testUpdate() {
        when(this.categoryRepository.findByIdIn(any())).thenReturn(new HashSet<>());

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

        Book book1 = new Book();
        book1.setIsbn("Isbn");
        book1.setId(123L);
        book1.setDeleted(true);
        book1.setPrice(BigDecimal.valueOf(42L));
        book1.setTitle("Dr");
        book1.setAuthor("JaneDoe");
        book1.setCoverImage("Cover Image");
        book1.setCategories(new HashSet<>());
        book1.setDescription("The characteristics of someone or something");
        Optional<Book> ofResult = Optional.of(book);
        when(this.bookRepository.save(any())).thenReturn(book1);
        when(this.bookRepository.findById(any())).thenReturn(ofResult);

        BookDto bookDto = new BookDto();
        bookDto.setIsbn("Isbn");
        bookDto.setCategoryIds(new HashSet<>());
        bookDto.setId(123L);
        bookDto.setPrice(BigDecimal.valueOf(42L));
        bookDto.setTitle("Dr");
        bookDto.setAuthor("JaneDoe");
        bookDto.setCoverImage("Cover Image");
        bookDto.setDescription("The characteristics of someone or something");
        when(this.bookMapper.toDto(any())).thenReturn(bookDto);

        CreateBookRequestDto createBookRequestDto = new CreateBookRequestDto();
        createBookRequestDto.setIsbn("Isbn");
        createBookRequestDto.setCategoryIds(new HashSet<>());
        createBookRequestDto.setPrice(BigDecimal.valueOf(42L));
        createBookRequestDto.setTitle("Dr");
        createBookRequestDto.setAuthor("JaneDoe");
        createBookRequestDto.setCoverImage("Cover Image");
        createBookRequestDto.setDescription("The characteristics of someone or something");
        BookDto actualUpdateResult = this.bookServiceImpl.update(123L, createBookRequestDto);
        assertSame(bookDto, actualUpdateResult);
        assertEquals("42", actualUpdateResult.getPrice().toString());
        verify(this.categoryRepository).findByIdIn(any());
        verify(this.bookRepository).findById(any());
        verify(this.bookRepository).save(any());
        verify(this.bookMapper).toDto(any());
    }

    @Test
    public void testFindAllByCategoryId() {
        when(this.bookRepository.findAllByCategoryId(any())).thenReturn(new ArrayList<>());
        this.bookServiceImpl.findAllByCategoryId(123L);
        verify(this.bookRepository).findAllByCategoryId(any());
    }
}
