package com.onlinebookstore.service;

import com.onlinebookstore.model.Book;
import java.util.List;

public interface BookService {
    Book save(Book product);

    List<Book> findAll();
}
