package com.nhnacademy.ailibraryteam3batch.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "books")
@Getter
@NoArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "book_sequence_generator")
    @SequenceGenerator(name = "book_sequence_generator",
            sequenceName = "public.book_sequence",
            allocationSize = 1000)
    private Long id;

    @NotNull
    @Column(nullable = false, length = 13, unique = true)
    private String isbn13;

    @Column(length = 25)
    private String volumnName;

    @NotNull
    @Column(nullable = false, length = 255)
    private String title;

    @NotNull
    @Column(nullable = false, length = 255)
    private String author;

    @Column(length = 100)
    private String publisherName;

    private LocalDate firstPublishedDate;

    @Column(length = 5)
    private String additionSymbol;

    private Integer price;

    @Column(length = 500)
    private String imageUrl;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(length = 25)
    private String kdcCode;

    @Column(length = 255)
    private String subTitle;

    @Column(length = 255)
    private String subAuthor;

    private LocalDate secondPublishedDate;

    private Boolean isBookstorePresent;

    private Boolean isPortalPresent;
}
