package com.onlinebookstore.service.impl;

import com.onlinebookstore.dto.BookDto;
import com.onlinebookstore.dto.BookSearchParameters;
import com.onlinebookstore.dto.CreateBookRequestDto;
import com.onlinebookstore.exception.EntityNotFoundException;
import com.onlinebookstore.mapper.BookMapper;
import com.onlinebookstore.model.Book;
import com.onlinebookstore.repository.book.BookRepository;
import com.onlinebookstore.repository.book.BookSpecificationBuilder;
import com.onlinebookstore.service.BookService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookSpecificationBuilder bookSpecificationBuilder;

    @Override
    public BookDto save(CreateBookRequestDto requestDto) {
        Book book = bookMapper.toModel(requestDto);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public List<BookDto> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable).stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public BookDto findById(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find books by id: " + id)
        );
        return bookMapper.toDto(book);
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public List<BookDto> search(BookSearchParameters parameters) {
        Specification<Book> build = bookSpecificationBuilder.build(parameters);
        return bookRepository.findAll(build)
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public BookDto update(Long id, CreateBookRequestDto requestDto) {
        Book bookById = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find books by id: " + id)
        );
        bookById.setTitle(requestDto.getTitle());
        bookById.setAuthor(requestDto.getAuthor());
        bookById.setIsbn(requestDto.getIsbn());
        bookById.setPrice(requestDto.getPrice());
        bookById.setDescription(requestDto.getDescription());
        bookById.setCoverImage(requestDto.getCoverImage());
        return bookMapper.toDto(bookRepository.save(bookById));
    }
}
