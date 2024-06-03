package com.learning.booktest.booktest.services.impl;

import com.learning.booktest.booktest.domain.Book;
import com.learning.booktest.booktest.domain.BookDTO;
import com.learning.booktest.booktest.domain.BookEntity;

public class TestData {

    public static Book testBook() {
        Book book = new Book();
        book.setIsbn("01234567");
        book.setAuthor("Johnny Silver");
        book.setTitle("The Seven Seas");
        return book;
    }

    public static BookEntity testBookEntity() {
        BookEntity bookEntity = new BookEntity();
        bookEntity.setIsbn("01234567");
        bookEntity.setAuthor("Johnny Silver");
        bookEntity.setTitle("The Seven Seas");
        return bookEntity;
    }

    public static BookDTO testBookDto(){
        BookDTO bookDTO = new BookDTO();
        bookDTO.setIsbn("01234567");
        bookDTO.setAuthor("Johnny Silver");
        bookDTO.setTitle("The Seven Seas");
        return bookDTO;
    }

}
