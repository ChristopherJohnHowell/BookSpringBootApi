package com.learning.booktest.booktest.services.impl;

import com.learning.booktest.booktest.domain.Book;
import com.learning.booktest.booktest.domain.BookEntity;
import com.learning.booktest.booktest.domain.utils.DomainUtils;
import com.learning.booktest.booktest.repository.BookRepository;
import com.learning.booktest.booktest.services.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Book save(Book book) {
        try {
            BookEntity result = bookRepository.save(DomainUtils.bookToBookEntity(book));
            return DomainUtils.bookEntityToBook(result);
        } catch (Exception err) {
            System.out.println("Service Save Error: " + err);
        }

        return null;
    }

    @Override
    public Optional<Book> update(Book book) {
        return ifBookExists(book.getIsbn()) ?
                Optional.of(DomainUtils.bookEntityToBook(bookRepository.save(DomainUtils.bookToBookEntity(book))))
                : Optional.empty();
    }

    @Override
    public Optional<Book> findById(String isbn) {
        Optional<BookEntity> foundBook = bookRepository.findById(isbn);
        return foundBook.map(DomainUtils::bookEntityToBook);
    }

    @Override
    public Optional<List<Book>> findAll() {
        List<BookEntity> allBookEntities = bookRepository.findAll();
        return allBookEntities.isEmpty() ? Optional.empty() : Optional.of(allBookEntities.stream().map(DomainUtils::bookEntityToBook).toList());
    }

    @Override
    public boolean ifBookExists(String isbn) {
        return findById(isbn).isPresent();
    }

    @Override
    public void deleteBookById(String isbn) {
        try {
            bookRepository.deleteById(isbn);
        } catch (EmptyResultDataAccessException e) {
            log.debug("Attempted to delete non-existing book", e);
        }
    }

//    private BookEntity bookToBookEntity(Book book) {
//        return BookEntity.builder()
//                .isbn(book.getIsbn())
//                .author(book.getAuthor())
//                .title(book.getTitle())
//                .build();
//    }
//
//    private Book bookEntityToBook(BookEntity bookEntity) {
//        return Book.builder()
//                .isbn(bookEntity.getIsbn())
//                .author(bookEntity.getAuthor())
//                .title(bookEntity.getTitle())
//                .build();
//    }

}
