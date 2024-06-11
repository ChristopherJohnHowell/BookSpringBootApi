package com.learning.booktest.booktest.testingUtils;

import com.learning.booktest.booktest.domain.Book;
import com.learning.booktest.booktest.domain.BookDTO;
import com.learning.booktest.booktest.domain.BookEntity;

public class TestData {

    public static Book testBookGood1() {
        Book book = new Book();
        book.setIsbn("01234567");
        book.setAuthor("Johnny Silver");
        book.setTitle("The Seven Seas");
        return book;
    }

    public static Book testBookBad1NotInDB() {
        Book book = new Book();
        book.setIsbn("987654321notInDb");
        book.setAuthor("Johnny Silver");
        book.setTitle("The Seven Seas");
        return book;
    }

    public static BookEntity testBookEntityGood1() {
        BookEntity bookEntity = new BookEntity();
        bookEntity.setIsbn("01234567");
        bookEntity.setAuthor("Johnny Silver");
        bookEntity.setTitle("The Seven Seas");
        return bookEntity;
    }

    public static BookDTO testBookDtoGood1() {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setIsbn("01234567");
        bookDTO.setAuthor("Johnny Silver");
        bookDTO.setTitle("The Seven Seas");
        return bookDTO;
    }

    public static BookDTO testBookDTOBadMissingData1() {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setIsbn("01234567");
        //bookDTO.setAuthor("Johnny Silver");
        bookDTO.setTitle("The Seven Seas");
        return bookDTO;
    }

    public static BookDTO testBookDTOBad2NotInDB() {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setIsbn("987654321notInDb");
        bookDTO.setAuthor("Johnny Silver");
        bookDTO.setTitle("The Seven Seas");
        return bookDTO;
    }

}
