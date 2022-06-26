package com.example.demomany.service;

import com.example.demomany.entity.Author;
import com.example.demomany.repository.AuthorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AuthorService {
    private final AuthorRepository repository;

    public AuthorService(AuthorRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Author create(Author author) {
        return repository.save(author);
    }

    @Transactional(readOnly = true)
    public List<Author>findAll() {
        return repository.findAll();
    }
}
