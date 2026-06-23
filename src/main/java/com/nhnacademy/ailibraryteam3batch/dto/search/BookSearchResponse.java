package com.nhnacademy.ailibraryteam3batch.dto.search;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 도서 검색 응답 class
@Getter
@NoArgsConstructor
public class BookSearchResponse {
    private Long id;
    private String isbn;
    private String title;
    private String author;
    private String publisherName;
    private Integer price;
    private String imageUrl;

    @QueryProjection // 응답 객체에 Querydsl 의존 발생
    public BookSearchResponse(Long id, String isbn, String title, String author, String publisherName, Integer price, String imageUrl) {
        this.id = id;
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.publisherName = publisherName;
        this.price = price;
        this.imageUrl = imageUrl;
    }
}
