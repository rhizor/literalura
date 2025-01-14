package com.literalura.Literalura.service;

import com.literalura.Literalura.model.AuthorEntity;
import com.literalura.Literalura.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    public List<AuthorEntity> findAuthorsAliveInYear(Integer year) {
        return authorRepository.findByBirthYearLessThanEqualAndDeathYearGreaterThanEqual(year, year);
    }

    public List<AuthorEntity> findAuthorsByName(String name) {
        return authorRepository.findByNameContainingIgnoreCase(name);
    }
    // Add more methods as needed
}
