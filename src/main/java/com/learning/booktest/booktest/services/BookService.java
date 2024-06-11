package com.learning.booktest.booktest.services;

import com.learning.booktest.booktest.domain.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {

    // Create
    Book save(Book book);

    // Update
    Optional<Book> update(Book book);

    // Read
    Optional<Book> findById(String isbn);

    Optional<List<Book>> findAll();

    // ifBookExists
    boolean ifBookExists(String isbn);

    // Delete
    void deleteBookById(String isbn);


}
