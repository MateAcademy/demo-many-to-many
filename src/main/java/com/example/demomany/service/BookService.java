package com.example.demomany.service;

import com.example.demomany.entity.Author;
import com.example.demomany.entity.Book;
import com.example.demomany.repository.AuthorRepository;
import com.example.demomany.repository.BookRepository;
import com.example.demomany.service.dto.BookDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    private final Logger logger = LoggerFactory.getLogger(BookService.class);

    private final BookRepository repository;
    private final AuthorRepository authorRepository;

    public BookService(BookRepository repository, AuthorRepository authorRepository) {
        this.repository = repository;
        this.authorRepository = authorRepository;
    }

    @Transactional
    public Optional<BookDto> create(BookDto bookDto) {
        List<Author> authors = new ArrayList<>();

        for (Long authorId : bookDto.getAuthorIds()) {
            final Optional<Author> authorOpt = authorRepository.findById(authorId);
            if (authorOpt.isEmpty()) {
                logger.error("Author with id {} doesn't exist", authorId);
                return Optional.empty();
            }
            authors.add(authorOpt.get());
        }

        final Book book = new Book();
        book.setName(bookDto.getName());
        book.setAuthors(authors);

        final Book saved = repository.save(book);

        final BookDto result = new BookDto();
        result.setId(saved.getId());
        result.setName(saved.getName());

        final List<Long> authorIds = new ArrayList<>(bookDto.getAuthorIds());
        result.setAuthorIds(authorIds);

        return Optional.of(result);
    }

    @Transactional(readOnly = true)
    public List<BookDto> findAll() {
        return repository.findAll().stream().map(book -> {
            final BookDto bookDto = new BookDto();
            bookDto.setId(book.getId());
            bookDto.setName(book.getName());
            bookDto.setAuthorIds(book.getAuthors().stream().map(Author::getId).toList());
            return bookDto;
        }).toList();
    }
}
