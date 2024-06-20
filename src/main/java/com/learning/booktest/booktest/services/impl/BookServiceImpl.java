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

    // Don't need @Autowired for Constructor Injection!
    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    /**
     * Save a new book.
     *
     * @param book The book to save.
     * @return The saved book, or null if an error occurs.
     */
    @Override
    public Book save(Book book) {
        try {
            BookEntity result = bookRepository.save(DomainUtils.bookToBookEntity(book));
            return DomainUtils.bookEntityToBook(result);
        } catch (Exception e) {
            log.error("Service Save Error: ", e);
            return null;
        }
    }

    /**
     * Update an existing book.
     *
     * @param book The book to update.
     * @return An Optional containing the updated book, or an empty Optional if the book does not exist or an error occurs.
     */
    @Override
    public Optional<Book> update(Book book) {
        try {
            return bookRepository.findById(book.getIsbn()).isPresent() ?
                    Optional.of(DomainUtils.bookEntityToBook(bookRepository.save(DomainUtils.bookToBookEntity(book))))
                    : Optional.empty();
        } catch (Exception e) {
            log.error("Service Update Error: ", e);
            return Optional.empty();
        }
    }

    /**
     * Find a book by its ISBN.
     *
     * @param isbn The ISBN of the book.
     * @return An Optional containing the book, or an empty Optional if the book does not exist.
     */
    @Override
    public Optional<Book> findById(String isbn) {
        Optional<BookEntity> foundBook = bookRepository.findById(isbn);
        return foundBook.map(DomainUtils::bookEntityToBook);
    }

    /**
     * Find all books.
     *
     * @return An Optional containing a list of all books, or an empty Optional if no books are found.
     */
    @Override
    public Optional<List<Book>> findAll() {
        List<BookEntity> allBookEntities = bookRepository.findAll();
        if (allBookEntities.isEmpty()) {
            return Optional.empty();
        } else {
            List<Book> allBooks = allBookEntities.stream().map(DomainUtils::bookEntityToBook).toList();
            return Optional.of(allBooks);
        }
    }

    /**
     * Delete a book by its ISBN.
     *
     * @param isbn The ISBN of the book to delete.
     */
    @Override
    public void deleteBookById(String isbn) {
        try {
            bookRepository.deleteById(isbn);
        } catch (EmptyResultDataAccessException e) {
            log.debug("Attempted to delete non-existing book with ISBN: {}. {1}", isbn, e);
        }
    }

}
