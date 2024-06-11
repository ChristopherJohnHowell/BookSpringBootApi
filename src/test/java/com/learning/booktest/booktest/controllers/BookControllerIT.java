package com.learning.booktest.booktest.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.booktest.booktest.domain.Book;
import com.learning.booktest.booktest.domain.BookDTO;
import com.learning.booktest.booktest.domain.utils.DomainUtils;
import com.learning.booktest.booktest.repository.BookRepository;
import com.learning.booktest.booktest.services.BookService;
import com.learning.booktest.booktest.testingUtils.TestData;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

// Remember "S.E.A.D" for annotations.
@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Transactional
public class BookControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookService bookService;

    @Autowired
    BookRepository bookRepository;

    @BeforeEach
    void setUp(){
        bookRepository.deleteAll();
        TestTransaction.flagForCommit();
        TestTransaction.end();
        TestTransaction.start();
    }

    @Test
    public void testIfCreateWithGoodDataReturnsHttpStatus201() throws Exception {
        BookDTO bookDTO = TestData.testBookDtoGood1();
        String jsonBookDto = new ObjectMapper().writeValueAsString(bookDTO);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/book/" + bookDTO.getIsbn()).contentType(MediaType.APPLICATION_JSON).content(jsonBookDto))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isbn").value(bookDTO.getIsbn()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(bookDTO.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author").value(bookDTO.getAuthor()));
    }

    @Test
    public void testIfCreateWithBadDataReturnsHttpStatus400() throws Exception {
        BookDTO badBookDTO = TestData.testBookDTOBadMissingData1();
        String jsonBookDto = new ObjectMapper().writeValueAsString(badBookDTO);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/book/" + badBookDTO.getIsbn()).contentType(MediaType.APPLICATION_JSON).content(jsonBookDto))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void testIfUpdateWithGoodDataReturnsHttpStatus200() throws Exception {
        BookDTO testBookDTO = TestData.testBookDtoGood1();
        Book book = DomainUtils.bookDtoToBook(testBookDTO);
        bookService.save(book);
        String bookStr = new ObjectMapper().writeValueAsString(testBookDTO);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/book/" + testBookDTO.getIsbn()).contentType(MediaType.APPLICATION_JSON).content(bookStr))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isbn").value(testBookDTO.getIsbn()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(testBookDTO.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author").value(testBookDTO.getAuthor()));
    }

    @Test
    public void testIfUpdateWithBadDataReturnsHttpStatus400() throws Exception {
        BookDTO testBookDTO = TestData.testBookDTOBadMissingData1();
        String bookStr = new ObjectMapper().writeValueAsString(testBookDTO);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/book/" + testBookDTO.getIsbn()).contentType(MediaType.APPLICATION_JSON).content(bookStr))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testFindBookWithBookExistingReturnsHttpStatusFound() throws Exception {
        final Book book = TestData.testBookGood1();
        bookService.save(book);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/book/" + book.getIsbn()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isbn").value(book.getIsbn()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author").value(book.getAuthor()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(book.getTitle()));
    }

    @Test
    public void testFindBookWithNoBookReturnsHttpStatusNotFound() throws Exception {
        Book testBook = TestData.testBookBad1NotInDB();
        mockMvc.perform(MockMvcRequestBuilders.get("/api/book/" + testBook.getIsbn()))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testThatDeleteBookReturnsHttpStatus204() throws Exception {
        Book book = TestData.testBookGood1();
        bookService.save(book);
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/book/" + book.getIsbn()))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void testThatDeleteBookBadDataReturnsHttpStatus204() throws Exception {
        BookDTO bookDTO = TestData.testBookDTOBad2NotInDB();
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/book/" + bookDTO.getIsbn()))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

//    @Test
//    public void testDeleteWholeDB(){
//        bookRepository.deleteAll();
//    }

}
