package com.learning.booktest.booktest.services;

import com.learning.booktest.booktest.domain.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {

    // Create / Update
    Book save(Book book);

    // Read
    Optional<Book> findById(String isbn);

    Optional<List<Book>> findAll();

    // ifBookExists
    boolean ifBookExists(Book book);

    // Delete
    void deleteBookById(String isbn);


}
