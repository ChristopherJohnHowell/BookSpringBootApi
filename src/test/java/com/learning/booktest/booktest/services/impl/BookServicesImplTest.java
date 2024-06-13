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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServicesImplTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl underTest;

    @Test
    public void saveBook_WithValidData_ShouldReturnBook() {
        BookEntity bookEntity = TestData.testBookEntityGood1();
        when(bookRepository.save(eq(bookEntity))).thenReturn(bookEntity);
        Book book = TestData.testBookGood1();
        Book result = underTest.save(book);
        assertThat(result).isEqualTo(book);
    }

    @Test
    public void saveBook_WithInvalidData_ShouldReturnNull() {
        BookEntity bookEntity = TestData.testBookEntityBad2();
        when(bookRepository.save(bookEntity)).thenReturn(null);
        Book book = DomainUtils.bookEntityToBook(bookEntity);
        System.out.println("Book:" + book);
        Book result = underTest.save(book);
        assertThat(result).isNull();
    }

    @Test
    public void findBookById_WithValidData_ShouldReturnBook() {
        final Book book = TestData.testBookGood1();
        final BookEntity bookEntity = TestData.testBookEntityGood1();
        when(bookRepository.findById(eq(book.getIsbn()))).thenReturn(Optional.of(bookEntity));
        Optional<Book> result = underTest.findById(book.getIsbn());
        assertThat(result).isPresent().contains(book);
    }

    @Test
    public void findBookById_WithInvalidData_ShouldReturnEmptyOptional() {
        final Book book = TestData.testBookBad2MissingData();
        when(bookRepository.findById(eq(book.getIsbn()))).thenReturn(Optional.empty());
        Optional<Book> result = underTest.findById(book.getIsbn());
        assertThat(result).isEmpty();
    }

    @Test
    public void findAllBooks_WithValidData_ShouldReturnBooks() {
        BookEntity testBookEntity = DomainUtils.bookToBookEntity(TestData.testBookGood1());
        List<BookEntity> bookEntities = new ArrayList<>();
        bookEntities.add(testBookEntity);
        when(bookRepository.findAll()).thenReturn(bookEntities);
        Optional<List<Book>> result = underTest.findAll();
        assertTrue(result.isPresent());
    }

    @Test
    public void findAllBooks_WithEmptyDB_ShouldReturnEmptyOptional() {
        when(bookRepository.findAll()).thenReturn(new ArrayList<>());
        Optional<List<Book>> result = underTest.findAll();
        assertThat(result).isEmpty();
    }

    @Test
    public void findAllBooks_WithMultipleBooks_ShouldReturnBooks() {
        BookEntity testBookEntity1 = DomainUtils.bookToBookEntity(TestData.testBookGood1());
        BookEntity testBookEntity2 = DomainUtils.bookToBookEntity(TestData.testBookGood2());
        List<BookEntity> bookEntities = List.of(testBookEntity1, testBookEntity2);
        when(bookRepository.findAll()).thenReturn(bookEntities);
        Optional<List<Book>> result = underTest.findAll();
        assertThat(result).isPresent();
        assertThat(result.get()).contains(DomainUtils.bookEntityToBook(testBookEntity1));
        assertThat(result.get()).contains(DomainUtils.bookEntityToBook(testBookEntity2));
    }

    @Test
    public void updateBook_WithValidData_ShouldReturnUpdatedBook() {
        String newTitle = "A new beginning!";
        Book testBook = TestData.testBookGood1();
        testBook.setTitle(newTitle);
        BookEntity testBookEntity = DomainUtils.bookToBookEntity(testBook);
        testBookEntity.setTitle(newTitle);
        when(bookRepository.findById(testBookEntity.getIsbn())).thenReturn(Optional.of(testBookEntity));
        when(bookRepository.save(eq(testBookEntity))).thenReturn(testBookEntity);
        Optional<Book> result = underTest.update(testBook);
        assertThat(result).isPresent();
        assertThat(result.get().getIsbn()).isEqualTo(testBook.getIsbn());
        assertThat(result.get().getTitle()).isEqualTo(testBook.getTitle());
        assertThat(result.get().getAuthor()).isEqualTo(testBook.getAuthor());
    }

    @Test
    public void updateBook_WithMissingIsbn_ShouldReturnEmptyOptional() {
        String newTitle = "A new beginning!";
        Book testBook = TestData.testBookBad3MissingIsbn();
        testBook.setTitle(newTitle);
        BookEntity testBookEntity = DomainUtils.bookToBookEntity(testBook);
        testBookEntity.setTitle(newTitle);
        when(bookRepository.findById(testBookEntity.getIsbn())).thenReturn(Optional.empty());
        Optional<Book> result = underTest.update(testBook);
        assertThat(result).isEmpty();
    }

    @Test
    public void updateBook_WithNoEntryInDb_ShouldReturnEmptyOptional() {
        String newTitle = "A new beginning!";
        Book testBook = TestData.testBookGood1();
        testBook.setTitle(newTitle);
        BookEntity testBookEntity = DomainUtils.bookToBookEntity(testBook);
        testBookEntity.setTitle(newTitle);
        when(bookRepository.findById(testBookEntity.getIsbn())).thenReturn(Optional.empty());
        Optional<Book> result = underTest.update(testBook);
        assertThat(result).isEmpty();
    }

    @Test
    public void updateBook_WithValidDataButInvalidIsbn_ShouldReturnEmptyOptional() {
        String newTitle = "A new beginning!";
        Book testBook = TestData.testBookGood1();
        testBook.setIsbn("invalid-isbn");
        testBook.setTitle(newTitle);
        when(bookRepository.findById(testBook.getIsbn())).thenReturn(Optional.empty());
        Optional<Book> result = underTest.update(testBook);
        assertThat(result).isEmpty();
    }

    @Test
    public void deleteBook_ShouldCallRepositoryDeleteByIdOnce() {
        final String isbn = "123123123";
        underTest.deleteBookById(isbn);
        verify(bookRepository, times(1)).deleteById(eq(isbn));
    }

    @Test
    public void deleteNonExistentBook_ShouldNotThrowException() {
        final String isbn = "non-existent-isbn";
        doNothing().when(bookRepository).deleteById(eq(isbn));
        underTest.deleteBookById(isbn);
        verify(bookRepository, times(1)).deleteById(eq(isbn));
    }

}
