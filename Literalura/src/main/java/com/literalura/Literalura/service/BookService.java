package com.literalura.Literalura.service;

import com.literalura.Literalura.model.Book;
import com.literalura.Literalura.model.BookEntity;
import com.literalura.Literalura.model.AuthorEntity;
import com.literalura.Literalura.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public List<BookEntity> findAllBooks() {
        return bookRepository.findAll();
    }

    public Long countBooksByLanguage(String language) {
        return bookRepository.countByLanguage(language);
    }

    public List<BookEntity> searchBooksByTitle(String title) {
        return bookRepository.findByTitleContaining(title);
    }

    public BookEntity convertToEntity(Book book) {
        BookEntity bookEntity = new BookEntity();
        bookEntity.setTitle(book.getTitle());
        if (!book.getLanguages().isEmpty()) {
            bookEntity.setLanguage(book.getLanguages().get(0)); // Asumimos que solo tomamos el primer idioma
        }
        bookEntity.setDownloads(book.getDownloadCount()); //

        if (!book.getAuthors().isEmpty()) {
            AuthorEntity authorEntity = convertAuthorToEntity(book.getAuthors().get(0)); // Solo el primer autor
            bookEntity.setAuthor(authorEntity);
        }
        return bookEntity;
    }

    private AuthorEntity convertAuthorToEntity(com.literalura.Literalura.model.Author author) {
        AuthorEntity authorEntity = new AuthorEntity();
        authorEntity.setName(author.getName());
        authorEntity.setBirthYear(author.getBirthYear()); //
        authorEntity.setDeathYear(author.getDeathYear()); //
        return authorEntity;
    }

    public void saveBook(Book book) {
        BookEntity bookEntity = convertToEntity(book);
        bookRepository.save(bookEntity);
    }

    public List<String> getLanguages() {
        return bookRepository.findDistinctLanguages();
    }

    public Long getBookCountByLanguage(String language) {
        return bookRepository.countByLanguage(language);
    }

    public void showStatistics() {
        List<String> languages = getLanguages();
        System.out.println("Estadísticas de libros por idioma:");
        for (String language : languages) {
            System.out.println(language + ": " + getBookCountByLanguage(language));
        }
    }

    public List<BookEntity> getTop10DownloadedBooks() {
        return bookRepository.findTop10ByOrderByDownloadsDesc();
    }
}