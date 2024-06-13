package com.learning.booktest.booktest.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "books")
//@SecondaryTable(name = "bookInfo") // For Secondary Tables do this with @Column()
public class BookEntity {

    @Id
    //@Column(updatable = true, nullable = false)
    private String isbn;

    private String author;

    //@Column(table = "bookInfo")
    private String title;
}
