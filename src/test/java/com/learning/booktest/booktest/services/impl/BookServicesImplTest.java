package com.learning.booktest.booktest.services.impl;

import com.learning.booktest.booktest.domain.Book;
import com.learning.booktest.booktest.domain.BookEntity;
import com.learning.booktest.booktest.domain.utils.DomainUtils;
import com.learning.booktest.booktest.repository.BookRepository;
import com.learning.booktest.booktest.testingUtils.TestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServicesImplTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl underTest;

    @Test
    public void testSaveBookWithGoodDataReturnsBook() {
        BookEntity bookEntity = TestData.testBookEntityGood1();
        when(bookRepository.save(eq(bookEntity))).thenReturn(bookEntity);
        Book book = TestData.testBookGood1();
        Book result = underTest.save(book);
        assertEquals(book, result);
    }

    @Test
    public void testSaveBookWithBadDataReturnsNull() {
        BookEntity bookEntity = TestData.testBookEntityBad2();
        when(bookRepository.save(bookEntity)).thenReturn(null);
        Book book = DomainUtils.bookEntityToBook(bookEntity);
        Book result = underTest.save(book);
        assertNull(result);
    }

    @Test
    public void testFindBookByIdWithGoodDataReturnsBook() {
        final Book book = TestData.testBookGood1();
        final BookEntity bookEntity = TestData.testBookEntityGood1();
        when(bookRepository.findById(eq(book.getIsbn()))).thenReturn(Optional.of(bookEntity));
        Optional<Book> result = underTest.findById(book.getIsbn());
        assertEquals(Optional.of(book), result);
    }

    @Test
    public void testFindBookByIdWithBadDataReturnsOptionalOfNull() {
        final Book book = TestData.testBookBad2MissingData();
        when(bookRepository.findById(eq(book.getIsbn()))).thenReturn(Optional.empty());
        Optional<Book> result = underTest.findById(book.getIsbn());
        assertEquals(Optional.empty(), result);
    }

    @Test
    public void testFindAllBooksWithGoodDataReturnsOptionalOfBooks() {
        BookEntity testBookEntity = DomainUtils.bookToBookEntity(TestData.testBookGood1());
        List<BookEntity> bookEntities = new ArrayList<>();
        bookEntities.add(testBookEntity);
        when(bookRepository.findAll()).thenReturn(bookEntities);
        Optional<List<Book>> result = underTest.findAll();
        assertTrue(result.isPresent());
    }

    @Test
    public void testFindAllBooksWithEmptyDBReturnsOptionalOfNull() {
        when(bookRepository.findAll()).thenReturn(new ArrayList<>());
        Optional<List<Book>> result = underTest.findAll();
        assertTrue(result.isEmpty());
    }

    @Test
    public void testUpdateBookWithGoodDataReturnsOptionalOfBook() {
        String newTitle = "A new beginning!";
        Book testBook = TestData.testBookGood1();
        testBook.setTitle(newTitle);
        BookEntity testBookEntity = DomainUtils.bookToBookEntity(testBook);
        testBookEntity.setTitle(newTitle);
        when(bookRepository.findById(testBookEntity.getIsbn())).thenReturn(Optional.of(testBookEntity));
        when(bookRepository.save(eq(testBookEntity))).thenReturn(testBookEntity);
        Optional<Book> result = underTest.update(testBook);
        assertTrue(result.isPresent());
        assertThat(result.get().getIsbn()).isEqualTo(testBook.getIsbn());
        assertThat(result.get().getTitle()).isEqualTo(testBook.getTitle());
        assertThat(result.get().getAuthor()).isEqualTo(testBook.getAuthor());
    }

    @Test
    public void testUpdateBookWithNoIsbnReturnsOptionalOfNull() {
        String newTitle = "A new beginning!";
        Book testBook = TestData.testBookBad3MissingIsbn();
        testBook.setTitle(newTitle);
        BookEntity testBookEntity = DomainUtils.bookToBookEntity(testBook);
        testBookEntity.setTitle(newTitle);
        when(bookRepository.findById(testBookEntity.getIsbn())).thenReturn(Optional.empty());
        Optional<Book> result = underTest.update(testBook);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testUpdateBookWithNoEntryInDbReturnsOptionalOfNull() {
        String newTitle = "A new beginning!";
        Book testBook = TestData.testBookGood1();
        testBook.setTitle(newTitle);
        BookEntity testBookEntity = DomainUtils.bookToBookEntity(testBook);
        testBookEntity.setTitle(newTitle);
        when(bookRepository.findById(testBookEntity.getIsbn())).thenReturn(Optional.empty());
        Optional<Book> result = underTest.update(testBook);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testDeleteBookDeletesBook1Time() {
        final String isbn = "123123123";
        underTest.deleteBookById(isbn);
        verify(bookRepository, times(1)).deleteById(eq(isbn));
    }

}
