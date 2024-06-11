package com.learning.booktest.booktest.domain;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookDTO {

    @Id
    private String isbn;

    @NotEmpty(message = "Author name is required")
    private String author;

    @NotEmpty(message = "Book Title is required")
    private String title;
}
