package com.learning.booktest.booktest.controllers;

import com.learning.booktest.booktest.domain.Book;
import com.learning.booktest.booktest.domain.BookDTO;
import com.learning.booktest.booktest.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class BookController {

    // AutoWired
    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    // GET ALL
    @GetMapping(path = "/books")
    public ResponseEntity<List<BookDTO>> retrieveBooks() {
        final Optional<List<Book>> foundBooks = bookService.findAll();
        Optional<List<BookDTO>> foundBookDTOs = foundBooks.map(cur -> cur.stream().map(this::bookToBookDto).toList());
        return foundBookDTOs.map(list -> new ResponseEntity<>(list, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    // GET ONE
    @GetMapping(path = "/books/{isbn}")
    public ResponseEntity<BookDTO> retrieveBook(@PathVariable final String isbn) {
        final Optional<Book> foundBook = bookService.findById(isbn);
        Optional<BookDTO> foundBookDTO = foundBook.map(this::bookToBookDto);
        return foundBookDTO.map(bookDTO -> new ResponseEntity<>(bookDTO, HttpStatus.OK)).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // CREATE ONE
    @PutMapping(path = "/books/{isbn}")
    public ResponseEntity<BookDTO> createUpdateBook(@PathVariable String isbn, @RequestBody BookDTO bookDTO) {
        bookDTO.setIsbn(isbn); // PathVariable ISBN set to match RequestBody ISBN.
        Book book = bookDtoToBook(bookDTO);
        boolean ifBookExists = bookService.ifBookExists(book);
        Book resultBook = bookService.save(book);
        BookDTO resultBookDTO = bookToBookDto(resultBook);
        return ifBookExists ?
                new ResponseEntity<>(resultBookDTO, HttpStatus.OK) : new ResponseEntity<>(resultBookDTO, HttpStatus.CREATED);
    }

    // DELETE
    @DeleteMapping(path = "/books/{isbn}")
    public ResponseEntity<Void> deleteBookById(@PathVariable final String isbn) {
        bookService.deleteBookById(isbn);
        Optional<Book> optionalBook = bookService.findById(isbn);
        if (optionalBook.isEmpty())
            return ResponseEntity.notFound().build();
        bookService.deleteBookById(isbn);
        return ResponseEntity.noContent().build();
    }

    private Book bookDtoToBook(BookDTO bookDTO) {
        return Book.builder()
                .isbn(bookDTO.getIsbn())
                .title(bookDTO.getTitle())
                .author(bookDTO.getAuthor())
                .build();
    }

    private BookDTO bookToBookDto(Book book) {
        return BookDTO.builder()
                .isbn(book.getIsbn())
                .title(book.getTitle())
                .author(book.getAuthor())
                .build();
    }

}
