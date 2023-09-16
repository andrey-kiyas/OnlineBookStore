package com.onlinebookstore.repository;

import com.onlinebookstore.model.Book;
import java.math.BigDecimal;
import java.util.HashSet;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

@DataJpaTest
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    @Test
    public void testFindAllByCategoryId() {
        Book book = new Book();
        book.setIsbn("42");
        book.setDeleted(true);
        book.setPrice(BigDecimal.valueOf(42L));
        book.setTitle("Dr");
        book.setAuthor("JaneDoe");
        book.setCoverImage("Cover Image");
        book.setCategories(new HashSet<>());
        book.setDescription("The characteristics of someone or something");

        Book book1 = new Book();
        book1.setIsbn("Isbn");
        book1.setDeleted(true);
        book1.setPrice(BigDecimal.valueOf(42L));
        book1.setTitle("Dr");
        book1.setAuthor("JaneDoe");
        book1.setCoverImage("Cover Image");
        book1.setCategories(new HashSet<>());
        book1.setDescription("The characteristics of someone or something");
        this.bookRepository.save(book);
        this.bookRepository.save(book1);
        Assertions.assertTrue(this.bookRepository.findAllByCategoryId(1L).isEmpty());
    }

    @Test
    public void testFindAllWithCategories() {
        Book book = new Book();
        book.setIsbn("42");
        book.setDeleted(true);
        book.setPrice(BigDecimal.valueOf(42L));
        book.setTitle("Dr");
        book.setAuthor("JaneDoe");
        book.setCoverImage("Cover Image");
        book.setCategories(new HashSet<>());
        book.setDescription("The characteristics of someone or something");

        Book book1 = new Book();
        book1.setIsbn("Isbn");
        book1.setDeleted(true);
        book1.setPrice(BigDecimal.valueOf(42L));
        book1.setTitle("Dr");
        book1.setAuthor("JaneDoe");
        book1.setCoverImage("Cover Image");
        book1.setCategories(new HashSet<>());
        book1.setDescription("The characteristics of someone or something");
        this.bookRepository.save(book);
        this.bookRepository.save(book1);

        Pageable pageable = PageRequest.of(0, 10);
        Assertions.assertTrue(this.bookRepository.findAllWithCategories(pageable).isEmpty());
    }

    @Test
    public void testFindAll() {
        Book book = new Book();
        book.setIsbn("42");
        book.setDeleted(true);
        book.setPrice(BigDecimal.valueOf(42L));
        book.setTitle("Dr");
        book.setAuthor("JaneDoe");
        book.setCoverImage("Cover Image");
        book.setCategories(new HashSet<>());
        book.setDescription("The characteristics of someone or something");

        Book book1 = new Book();
        book1.setIsbn("Isbn");
        book1.setDeleted(true);
        book1.setPrice(BigDecimal.valueOf(42L));
        book1.setTitle("Dr");
        book1.setAuthor("JaneDoe");
        book1.setCoverImage("Cover Image");
        book1.setCategories(new HashSet<>());
        book1.setDescription("The characteristics of someone or something");
        this.bookRepository.save(book);
        this.bookRepository.save(book1);

        Pageable pageable = PageRequest.of(0, 10);
        Assertions.assertTrue(this.bookRepository.findAll(pageable).isEmpty());
    }
}
