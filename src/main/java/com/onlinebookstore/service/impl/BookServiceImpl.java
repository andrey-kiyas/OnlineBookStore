package com.onlinebookstore.service.impl;

import com.onlinebookstore.repository.BookRepository;
import com.onlinebookstore.model.Book;
import com.onlinebookstore.service.BookService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Book save(Book product) {
        return bookRepository.save(product);
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }
}
