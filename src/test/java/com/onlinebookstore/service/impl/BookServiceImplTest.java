package com.onlinebookstore.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
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
import java.util.Set;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
        when(this.categoryRepository.findByIdIn((Set<Long>) any())).thenReturn(new HashSet<>());

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
        when(this.bookRepository.save((Book) any())).thenReturn(book);

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
        when(this.bookMapper.toDto((Book) any())).thenReturn(bookDto);
        when(this.bookMapper.toModel((CreateBookRequestDto) any())).thenReturn(book1);

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
        verify(this.categoryRepository).findByIdIn((Set<Long>) any());
        verify(this.bookRepository).save((Book) any());
        verify(this.bookMapper).toDto((Book) any());
        verify(this.bookMapper).toModel((CreateBookRequestDto) any());
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
        when(bookRepository.save(Mockito.<Book>any())).thenReturn(book);

        Book book2 = new Book();
        book2.setAuthor("JaneDoe");
        book2.setCategories(new HashSet<>());
        book2.setCoverImage("Cover Image");
        book2.setDeleted(true);
        book2.setDescription("The characteristics of someone or something");
        book2.setId(1L);
        book2.setIsbn("Isbn");
        book2.setPrice(BigDecimal.valueOf(1L));
        book2.setTitle("Dr");

        BookDto bookDto = new BookDto();
        bookDto.setAuthor("JaneDoe");
        bookDto.setCategoryIds(new HashSet<>());
        bookDto.setCoverImage("Cover Image");
        bookDto.setDescription("The characteristics of someone or something");
        bookDto.setId(1L);
        bookDto.setIsbn("Isbn");
        bookDto.setPrice(BigDecimal.valueOf(1L));
        bookDto.setTitle("Dr");
        when(bookMapper.toDto(Mockito.<Book>any())).thenReturn(bookDto);
        when(bookMapper.toModel(Mockito.<CreateBookRequestDto>any())).thenReturn(book2);
        when(categoryRepository.findByIdIn(Mockito.<Set<Long>>any())).thenReturn(new HashSet<>());

        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setAuthor("JaneDoe");
        requestDto.setCategoryIds(new HashSet<>());
        requestDto.setCoverImage("Cover Image");
        requestDto.setDescription("The characteristics of someone or something");
        requestDto.setIsbn("Isbn");
        requestDto.setPrice(BigDecimal.valueOf(1L));
        requestDto.setTitle("Dr");
        BookDto actualSaveResult = bookServiceImpl.save(requestDto);
        assertSame(bookDto, actualSaveResult);
        assertEquals("1", actualSaveResult.getPrice().toString());
        verify(bookRepository).save(Mockito.<Book>any());
        verify(bookMapper).toDto(Mockito.<Book>any());
        verify(bookMapper).toModel(Mockito.<CreateBookRequestDto>any());
        verify(categoryRepository).findByIdIn(Mockito.<Set<Long>>any());
    }

    @Test
    public void testSave3() {
        when(categoryRepository.findByIdIn(Mockito.<Set<Long>>any()))
                .thenThrow(new EntityNotFoundException("An error occurred"));

        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setAuthor("JaneDoe");
        requestDto.setCategoryIds(new HashSet<>());
        requestDto.setCoverImage("Cover Image");
        requestDto.setDescription("The characteristics of someone or something");
        requestDto.setIsbn("Isbn");
        requestDto.setPrice(BigDecimal.valueOf(1L));
        requestDto.setTitle("Dr");
        assertThrows(EntityNotFoundException.class, () -> bookServiceImpl.save(requestDto));
        verify(categoryRepository).findByIdIn(Mockito.<Set<Long>>any());
    }

    @Test
    public void testFindAll2() {
        when(bookRepository.findAllWithCategories(Mockito.<Pageable>any())).thenReturn(new ArrayList<>());
        assertTrue(bookServiceImpl.findAll(null).isEmpty());
        verify(bookRepository).findAllWithCategories(Mockito.<Pageable>any());
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
        when(this.bookRepository.findBookById((Long) any())).thenReturn(ofResult);

        BookDto bookDto = new BookDto();
        bookDto.setIsbn("Isbn");
        bookDto.setCategoryIds(new HashSet<>());
        bookDto.setId(123L);
        bookDto.setPrice(BigDecimal.valueOf(42L));
        bookDto.setTitle("Dr");
        bookDto.setAuthor("JaneDoe");
        bookDto.setCoverImage("Cover Image");
        bookDto.setDescription("The characteristics of someone or something");
        when(this.bookMapper.toDto((Book) any())).thenReturn(bookDto);
        BookDto actualFindByIdResult = this.bookServiceImpl.findById(123L);
        assertSame(bookDto, actualFindByIdResult);
        assertEquals("42", actualFindByIdResult.getPrice().toString());
        verify(this.bookRepository).findBookById((Long) any());
        verify(this.bookMapper).toDto((Book) any());
    }

    @Test
    public void testFindById3() {
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
        Optional<Book> ofResult = Optional.of(book);
        when(bookRepository.findBookById(Mockito.<Long>any())).thenReturn(ofResult);

        BookDto bookDto = new BookDto();
        bookDto.setAuthor("JaneDoe");
        bookDto.setCategoryIds(new HashSet<>());
        bookDto.setCoverImage("Cover Image");
        bookDto.setDescription("The characteristics of someone or something");
        bookDto.setId(1L);
        bookDto.setIsbn("Isbn");
        bookDto.setPrice(BigDecimal.valueOf(1L));
        bookDto.setTitle("Dr");
        when(bookMapper.toDto(Mockito.<Book>any())).thenReturn(bookDto);
        BookDto actualFindByIdResult = bookServiceImpl.findById(1L);
        assertSame(bookDto, actualFindByIdResult);
        assertEquals("1", actualFindByIdResult.getPrice().toString());
        verify(bookRepository).findBookById(Mockito.<Long>any());
        verify(bookMapper).toDto(Mockito.<Book>any());
    }

    @Test
    public void testFindById4() {
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
        Optional<Book> ofResult = Optional.of(book);
        when(bookRepository.findBookById(Mockito.<Long>any())).thenReturn(ofResult);
        when(bookMapper.toDto(Mockito.<Book>any())).thenThrow(new EntityNotFoundException("An error occurred"));
        assertThrows(EntityNotFoundException.class, () -> bookServiceImpl.findById(1L));
        verify(bookRepository).findBookById(Mockito.<Long>any());
        verify(bookMapper).toDto(Mockito.<Book>any());
    }

    @Test
    public void testFindById5() {
        Optional<Book> emptyResult = Optional.empty();
        when(bookRepository.findBookById(Mockito.<Long>any())).thenReturn(emptyResult);
        assertThrows(EntityNotFoundException.class, () -> bookServiceImpl.findById(1L));
        verify(bookRepository).findBookById(Mockito.<Long>any());
    }

    @Test
    public void testDeleteById() {
        doNothing().when(this.bookRepository).deleteById((Long) any());
        this.bookServiceImpl.deleteById(123L);
        verify(this.bookRepository).deleteById((Long) any());
    }

    @Test
    public void testDeleteById2() {
        doNothing().when(bookRepository).deleteById(Mockito.<Long>any());
        bookServiceImpl.deleteById(1L);
        verify(bookRepository).deleteById(Mockito.<Long>any());
    }

    @Test
    public void testDeleteById3() {
        doThrow(new EntityNotFoundException("An error occurred")).when(bookRepository).deleteById(Mockito.<Long>any());
        assertThrows(EntityNotFoundException.class, () -> bookServiceImpl.deleteById(1L));
        verify(bookRepository).deleteById(Mockito.<Long>any());
    }

    @Test
    public void testSearch() {
        when(this.bookSpecificationBuilder.build((BookSearchParameters) any())).thenReturn(null);
        when(this.bookRepository.findAll((Specification<Book>) any())).thenReturn(new ArrayList<>());
        this.bookServiceImpl.search(null);
    }

    @Test
    public void testSearch2() {
        when(bookSpecificationBuilder.build(Mockito.<BookSearchParameters>any()))
                .thenThrow(new EntityNotFoundException("An error occurred"));
        assertThrows(EntityNotFoundException.class,
                () -> bookServiceImpl.search(new BookSearchParameters(new String[]{"Dr"}, new String[]{"JaneDoe"},
                        new String[]{"Isbn"}, new String[]{"Price"}, new String[]{"The characteristics of someone or something"},
                        new String[]{"Categories"})));
        verify(bookSpecificationBuilder).build(Mockito.<BookSearchParameters>any());
    }

    @Test
    public void testSearch3() {
        when(bookRepository.findAll(Mockito.<Specification<Book>>any())).thenReturn(new ArrayList<>());
        when(bookSpecificationBuilder.build(Mockito.<BookSearchParameters>any())).thenReturn(null);
        assertTrue(bookServiceImpl.search(new BookSearchParameters(new String[]{"Dr"}, new String[]{"JaneDoe"},
                new String[]{"Isbn"}, new String[]{"Price"}, new String[]{"The characteristics of someone or something"},
                new String[]{"Categories"})).isEmpty());
        verify(bookRepository).findAll(Mockito.<Specification<Book>>any());
        verify(bookSpecificationBuilder).build(Mockito.<BookSearchParameters>any());
    }

    @Test
    public void testUpdate() {
        when(this.categoryRepository.findByIdIn((Set<Long>) any())).thenReturn(new HashSet<>());

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
        when(this.bookRepository.save((Book) any())).thenReturn(book1);
        when(this.bookRepository.findById((Long) any())).thenReturn(ofResult);

        BookDto bookDto = new BookDto();
        bookDto.setIsbn("Isbn");
        bookDto.setCategoryIds(new HashSet<>());
        bookDto.setId(123L);
        bookDto.setPrice(BigDecimal.valueOf(42L));
        bookDto.setTitle("Dr");
        bookDto.setAuthor("JaneDoe");
        bookDto.setCoverImage("Cover Image");
        bookDto.setDescription("The characteristics of someone or something");
        when(this.bookMapper.toDto((Book) any())).thenReturn(bookDto);

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
        verify(this.categoryRepository).findByIdIn((Set<Long>) any());
        verify(this.bookRepository).findById((Long) any());
        verify(this.bookRepository).save((Book) any());
        verify(this.bookMapper).toDto((Book) any());
    }

    @Test
    public void testUpdate3() {
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
        Optional<Book> ofResult = Optional.of(book);

        Book book2 = new Book();
        book2.setAuthor("JaneDoe");
        book2.setCategories(new HashSet<>());
        book2.setCoverImage("Cover Image");
        book2.setDeleted(true);
        book2.setDescription("The characteristics of someone or something");
        book2.setId(1L);
        book2.setIsbn("Isbn");
        book2.setPrice(BigDecimal.valueOf(1L));
        book2.setTitle("Dr");
        when(bookRepository.save(Mockito.<Book>any())).thenReturn(book2);
        when(bookRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        BookDto bookDto = new BookDto();
        bookDto.setAuthor("JaneDoe");
        bookDto.setCategoryIds(new HashSet<>());
        bookDto.setCoverImage("Cover Image");
        bookDto.setDescription("The characteristics of someone or something");
        bookDto.setId(1L);
        bookDto.setIsbn("Isbn");
        bookDto.setPrice(BigDecimal.valueOf(1L));
        bookDto.setTitle("Dr");
        when(bookMapper.toDto(Mockito.<Book>any())).thenReturn(bookDto);
        when(categoryRepository.findByIdIn(Mockito.<Set<Long>>any())).thenReturn(new HashSet<>());

        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setAuthor("JaneDoe");
        requestDto.setCategoryIds(new HashSet<>());
        requestDto.setCoverImage("Cover Image");
        requestDto.setDescription("The characteristics of someone or something");
        requestDto.setIsbn("Isbn");
        requestDto.setPrice(BigDecimal.valueOf(1L));
        requestDto.setTitle("Dr");
        BookDto actualUpdateResult = bookServiceImpl.update(1L, requestDto);
        assertSame(bookDto, actualUpdateResult);
        assertEquals("1", actualUpdateResult.getPrice().toString());
        verify(bookRepository).save(Mockito.<Book>any());
        verify(bookRepository).findById(Mockito.<Long>any());
        verify(bookMapper).toDto(Mockito.<Book>any());
        verify(categoryRepository).findByIdIn(Mockito.<Set<Long>>any());
    }

    @Test
    public void testUpdate4() {
        when(categoryRepository.findByIdIn(Mockito.<Set<Long>>any()))
                .thenThrow(new EntityNotFoundException("An error occurred"));

        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setAuthor("JaneDoe");
        requestDto.setCategoryIds(new HashSet<>());
        requestDto.setCoverImage("Cover Image");
        requestDto.setDescription("The characteristics of someone or something");
        requestDto.setIsbn("Isbn");
        requestDto.setPrice(BigDecimal.valueOf(1L));
        requestDto.setTitle("Dr");
        assertThrows(EntityNotFoundException.class, () -> bookServiceImpl.update(1L, requestDto));
        verify(categoryRepository).findByIdIn(Mockito.<Set<Long>>any());
    }

    @Test
    public void testUpdate5() {
        Optional<Book> emptyResult = Optional.empty();
        when(bookRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);
        when(categoryRepository.findByIdIn(Mockito.<Set<Long>>any())).thenReturn(new HashSet<>());

        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setAuthor("JaneDoe");
        requestDto.setCategoryIds(new HashSet<>());
        requestDto.setCoverImage("Cover Image");
        requestDto.setDescription("The characteristics of someone or something");
        requestDto.setIsbn("Isbn");
        requestDto.setPrice(BigDecimal.valueOf(1L));
        requestDto.setTitle("Dr");
        assertThrows(EntityNotFoundException.class, () -> bookServiceImpl.update(1L, requestDto));
        verify(bookRepository).findById(Mockito.<Long>any());
        verify(categoryRepository).findByIdIn(Mockito.<Set<Long>>any());
    }

    @Test
    public void testFindAllByCategoryId() {
        when(this.bookRepository.findAllByCategoryId((Long) any())).thenReturn(new ArrayList<>());
        this.bookServiceImpl.findAllByCategoryId(123L);
        verify(this.bookRepository).findAllByCategoryId((Long) any());
    }

    @Test
    public void testFindAllByCategoryId2() {
        when(bookRepository.findAllByCategoryId(Mockito.<Long>any())).thenReturn(new ArrayList<>());
        assertTrue(bookServiceImpl.findAllByCategoryId(1L).isEmpty());
        verify(bookRepository).findAllByCategoryId(Mockito.<Long>any());
    }
}
