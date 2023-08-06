package com.onlinebookstore.dao;

import com.onlinebookstore.model.Book;
import java.util.List;

public interface BookRepository {
    Book save(Book product);

    List<Book> findAll();
}
