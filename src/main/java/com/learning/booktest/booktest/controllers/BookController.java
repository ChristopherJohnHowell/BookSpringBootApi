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
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@RestController
public class BookController {

    // For AutoWired.
    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    // CREATE ONE
    @PostMapping(path = "/api/book/{isbn}")
    public ResponseEntity<?> createBook(@PathVariable String isbn, @Valid @RequestBody BookDTO bookDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<ObjectError> allErrors = bindingResult.getAllErrors();
            Stream<String> errMessages = allErrors.stream().map(DefaultMessageSourceResolvable::getDefaultMessage);
            return new ResponseEntity<>(errMessages, HttpStatus.BAD_REQUEST);
        }

        bookDTO.setIsbn(isbn); // PathVariable ISBN set to match RequestBody ISBN.
        Book book = DomainUtils.bookDtoToBook(bookDTO);
        Book resultBook = bookService.save(book);
        BookDTO resultBookDTO = DomainUtils.bookToBookDto(resultBook);
        return new ResponseEntity<>(resultBookDTO, HttpStatus.CREATED);
    }

    // GET ALL
    @GetMapping(path = "/api/book")
    public ResponseEntity<List<BookDTO>> getBooks() {
        final Optional<List<Book>> foundBooks = bookService.findAll();
        Optional<List<BookDTO>> foundBookDTOs = foundBooks.map(cur -> cur.stream().map(DomainUtils::bookToBookDto).toList());
        return foundBookDTOs.map(list -> new ResponseEntity<>(list, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    // GET ONE
    @GetMapping(path = "/api/book/{isbn}")
    public ResponseEntity<BookDTO> getBook(@PathVariable final String isbn) {
        final Optional<Book> foundBook = bookService.findById(isbn);
        Optional<BookDTO> foundBookDTO = foundBook.map(DomainUtils::bookToBookDto);
        return foundBookDTO.map(bookDTO -> new ResponseEntity<>(bookDTO, HttpStatus.OK)).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // UPDATE ONE
    @PutMapping(path = "/api/book/{isbn}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable String isbn, @RequestBody BookDTO bookDTO) {
        bookDTO.setIsbn(isbn); // PathVariable ISBN set to match RequestBody ISBN.
        Book book = DomainUtils.bookDtoToBook(bookDTO);
        Optional<Book> updatedBook = bookService.update(book);
        if (updatedBook.isEmpty())
            return ResponseEntity.notFound().build();

        BookDTO resultBookDTO = DomainUtils.bookToBookDto(updatedBook.get());
        return new ResponseEntity<>(resultBookDTO, HttpStatus.OK);
    }

    // DELETE ONE
    @DeleteMapping(path = "/api/book/{isbn}")
    public ResponseEntity<Void> deleteBookById(@PathVariable final String isbn) {
        boolean bookExists = bookService.ifBookExists(isbn);
        if (!bookExists)
            return ResponseEntity.notFound().build();
        bookService.deleteBookById(isbn);
        return ResponseEntity.noContent().build();
    }

//    private Book bookDtoToBook(BookDTO bookDTO) {
//        return Book.builder()
//                .isbn(bookDTO.getIsbn())
//                .title(bookDTO.getTitle())
//                .author(bookDTO.getAuthor())
//                .build();
//    }
//
//    private BookDTO bookToBookDto(Book book) {
//        return BookDTO.builder()
//                .isbn(book.getIsbn())
//                .title(book.getTitle())
//                .author(book.getAuthor())
//                .build();
//    }

}
