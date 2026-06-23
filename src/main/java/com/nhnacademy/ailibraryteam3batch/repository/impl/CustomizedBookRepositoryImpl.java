package com.nhnacademy.ailibraryteam3batch.repository.impl;

import com.nhnacademy.ailibraryteam3batch.domain.QBook;
import com.nhnacademy.ailibraryteam3batch.dto.search.BookSearchRequest;
import com.nhnacademy.ailibraryteam3batch.dto.search.BookSearchResponse;
import com.nhnacademy.ailibraryteam3batch.dto.search.QBookSearchResponse;
import com.nhnacademy.ailibraryteam3batch.repository.CustomizedBookRepository;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.util.StringUtils.hasText;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomizedBookRepositoryImpl implements CustomizedBookRepository {
    private final JPAQueryFactory queryFactory;
    private final QBook book = QBook.book;

    @Override
    public Page<BookSearchResponse> search(Pageable pageable, BookSearchRequest request) {
        List<BookSearchResponse> result = queryFactory.select(new QBookSearchResponse(
                book.id,
                book.isbn13,
                book.title,
                book.author,
                book.publisherName,
                book.price,
                book.imageUrl
        )).from(book)
          .where(keywordContains(request.keyword()), isbnEq(request.isbn()))
          .offset(pageable.getOffset())
          .limit(pageable.getPageSize())
          .fetch();

        long total = queryFactory
                .select(book.count())
                .from(book)
                .where(keywordContains(request.keyword()), isbnEq(request.isbn()))
                .fetchOne();

        return new PageImpl<>(result, pageable, total);
    }

    // TODO-R BooleanBuilder vs Predicate
    private Predicate keywordContains(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return null;
        }

        return book.title.containsIgnoreCase(keyword)
                .or(book.author.containsIgnoreCase(keyword));
    }

    private Predicate isbnEq(String isbn) {
        if (!hasText(isbn)) return null;

        return book.isbn13.eq(isbn);
    }
}
