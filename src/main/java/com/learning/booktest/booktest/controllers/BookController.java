package com.learning.booktest.booktest.controllers;

import com.learning.booktest.booktest.domain.Book;
import com.learning.booktest.booktest.domain.BookDTO;
import com.learning.booktest.booktest.domain.utils.DomainUtils;
import com.learning.booktest.booktest.services.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/book")
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    /**
     * Create a new book.
     *
     * @param isbn          The ISBN of the book.
     * @param bookDTO       The book data transfer object.
     * @param bindingResult The binding result for validation errors.
     * @return A ResponseEntity with the created book or error messages.
     */
    @PostMapping(path = "/{isbn}")
    public ResponseEntity<?> createBook(@PathVariable String isbn, @Valid @RequestBody BookDTO bookDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errorMessages = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            return new ResponseEntity<>(errorMessages, HttpStatus.BAD_REQUEST);
        }

        bookDTO.setIsbn(isbn); // PathVariable ISBN set to match RequestBody ISBN.
        Book book = DomainUtils.bookDtoToBook(bookDTO);
        Book savedBook = bookService.save(book);

        if (savedBook == null) {
            return new ResponseEntity<>("Failed to save book.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        BookDTO resultBookDTO = DomainUtils.bookToBookDto(savedBook);

        return new ResponseEntity<>(resultBookDTO, HttpStatus.CREATED);
    }

    /**
     * Get all books.
     *
     * @return A ResponseEntity with a list of all books or a no content status.
     */
    @GetMapping
    public ResponseEntity<List<BookDTO>> getBooks() {
        final Optional<List<Book>> foundBooks = bookService.findAll();

        if (foundBooks.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        List<BookDTO> bookDTOs = foundBooks.get()
                .stream()
                .map(DomainUtils::bookToBookDto)
                .toList();

        return new ResponseEntity<>(bookDTOs, HttpStatus.OK);
    }

    /**
     * Get a book by its ISBN.
     *
     * @param isbn The ISBN of the book.
     * @return A ResponseEntity with the found book or a not found status.
     */
    @GetMapping(path = "/{isbn}")
    public ResponseEntity<BookDTO> getBook(@PathVariable final String isbn) {
        final Optional<Book> foundBook = bookService.findById(isbn);

        if (foundBook.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        BookDTO foundBookDTO = DomainUtils.bookToBookDto(foundBook.get());

        return new ResponseEntity<>(foundBookDTO, HttpStatus.OK);
    }

    /**
     * Update an existing book.
     *
     * @param isbn    The ISBN of the book.
     * @param bookDTO The book data transfer object.
     * @return A ResponseEntity with the updated book or a not found status.
     */
    @PutMapping(path = "/{isbn}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable String isbn, @RequestBody BookDTO bookDTO) {
        bookDTO.setIsbn(isbn); // PathVariable ISBN set to match RequestBody ISBN.
        Book book = DomainUtils.bookDtoToBook(bookDTO);
        Optional<Book> updatedBook = bookService.update(book);

        if (updatedBook.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        BookDTO resultBookDTO = DomainUtils.bookToBookDto(updatedBook.get());

        return new ResponseEntity<>(resultBookDTO, HttpStatus.OK);
    }

    /**
     * Delete a book by its ISBN.
     *
     * @param isbn The ISBN of the book.
     * @return A ResponseEntity with no content or a not found status.
     */
    @DeleteMapping(path = "/{isbn}")
    public ResponseEntity<Void> deleteBookById(@PathVariable final String isbn) {
        if (bookService.findById(isbn).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        bookService.deleteBookById(isbn);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
