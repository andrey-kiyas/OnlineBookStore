package com.onlinebookstore.service;

import com.onlinebookstore.dto.BookDto;
import com.onlinebookstore.dto.BookSearchParameters;
import com.onlinebookstore.dto.CreateBookRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    List<BookDto> findAll(Pageable pageable);

    BookDto findById(Long id);

    void deleteById(Long id);

    List<BookDto> search(BookSearchParameters parameters);

    BookDto update(Long id, CreateBookRequestDto requestDto);
}
