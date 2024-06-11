package com.learning.booktest.booktest.services;

import com.learning.booktest.booktest.domain.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {

    // Create
    Book save(Book book);

    // Read
    Optional<Book> findById(String isbn);

    // Read
    Optional<List<Book>> findAll();

    // Update
    Optional<Book> update(Book book);

    // Delete
    void deleteBookById(String isbn);

    // ifBookExists
    boolean ifBookExists(String isbn);

}
