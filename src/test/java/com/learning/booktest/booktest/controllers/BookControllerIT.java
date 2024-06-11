package com.learning.booktest.booktest.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.booktest.booktest.domain.Book;
import com.learning.booktest.booktest.domain.BookDTO;
import com.learning.booktest.booktest.services.BookService;
import com.learning.booktest.booktest.services.impl.TestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

// Remember "S.E.A.D" for annotations.
@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class BookControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookService bookService;

    @Test
    public void testIfCreateBookReturnsHttpStatus201() throws Exception {
        BookDTO bookDTO = TestData.testBookDto();
        String jsonBookDto = new ObjectMapper().writeValueAsString(bookDTO);

        mockMvc.perform(MockMvcRequestBuilders.put("api/books/" + bookDTO.getIsbn()).content(jsonBookDto).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isbn").value(bookDTO.getIsbn()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(bookDTO.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author").value(bookDTO.getAuthor()));
    }

    @Test
    public void testThatRetrievingBookReturnsHttpStatusFound() throws Exception {
        final Book book = TestData.testBook();
        bookService.save(book);
        mockMvc.perform(MockMvcRequestBuilders.get("api/books/" + book.getIsbn()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isbn").value(book.getIsbn()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author").value(book.getAuthor()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(book.getTitle()));
    }

    @Test
    public void testThatDeleteBookReturnsHttpStatus204() throws Exception {
        Book book = TestData.testBook();
        mockMvc.perform(MockMvcRequestBuilders.delete("api/books/" + book.getIsbn()))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

}
