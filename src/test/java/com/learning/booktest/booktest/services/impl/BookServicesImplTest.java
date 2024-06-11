package com.learning.booktest.booktest.services.impl;

import com.learning.booktest.booktest.domain.Book;
import com.learning.booktest.booktest.domain.BookEntity;
import com.learning.booktest.booktest.repository.BookRepository;
import com.learning.booktest.booktest.testingUtils.TestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServicesImplTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl underTest;

    @Test
    public void testToSeeIfNullIsReturnedWhenTestingBookCreation() {
        BookEntity bookEntity = TestData.testBookEntityGood1();
        when(bookRepository.save(eq(bookEntity))).thenReturn(bookEntity);
        Book book = TestData.testBookGood1();
        Book result = underTest.save(book);
        assertEquals(book, result);
    }

    @Test
    public void testIfBookReturnedForTestBook() {
        final Book book = TestData.testBookGood1();
        final BookEntity bookEntity = TestData.testBookEntityGood1();
        when(bookRepository.findById(eq(book.getIsbn()))).thenReturn(Optional.of(bookEntity));
        Optional<Book> result = underTest.findById(book.getIsbn());
        assertEquals(Optional.of(book), result);
    }

    @Test
    public void testDeleteBookDeletesBook(){
        final String isbn = "123123123";
        underTest.deleteBookById(isbn);
        verify(bookRepository, times(1)).deleteById(eq(isbn));
    }


}
