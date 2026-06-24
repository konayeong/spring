package com.nhnacademy.ailibraryteam3batch.repository.impl;

import com.nhnacademy.ailibraryteam3batch.domain.QBook;
import com.nhnacademy.ailibraryteam3batch.dto.search.BookSearchResponse;
import com.nhnacademy.ailibraryteam3batch.dto.search.QBookSearchResponse;
import com.nhnacademy.ailibraryteam3batch.repository.CustomizedBookRepository;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import static org.springframework.util.StringUtils.hasText;

@Slf4j
@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomizedBookRepositoryImpl implements CustomizedBookRepository {
    private final JPAQueryFactory queryFactory;
    private final QBook book = QBook.book;

    @Override
    public Page<BookSearchResponse> searchAll(Pageable pageable) {
        return search(pageable, null);
    }

    @Override
    public Page<BookSearchResponse> searchByKeyword(Pageable pageable, String keyword) {
        return search(pageable, keywordContains(keyword));
    }

    @Override
    public Page<BookSearchResponse> searchByIsbn(Pageable pageable, String isbn) {
        return search(pageable, isbnEq(isbn));
    }

    private Page<BookSearchResponse> search(Pageable pageable, Predicate condition) {
        log.info("검색 Querydsl 작성");
        List<BookSearchResponse> content =
                queryFactory.select(bookProjection())
                        .from(book)
                        .where(condition)
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetch();
        Long total = queryFactory.select(book.count())
                .from(book)
                .where(condition)
                .fetchOne();

        return new PageImpl<>(content, pageable, (total == null ? 0L : total));
    }

    private QBookSearchResponse bookProjection() {
        log.info("BookProjection");
        return new QBookSearchResponse(
                book.id,
                book.isbn13,
                book.title,
                book.author,
                book.publisherName,
                book.price,
                book.imageUrl,
                book.volumeName
        );
    }

    // Predicate: 고정된 검색 조건 표현
    // BooleanBuilder: 사용자 입력에 따라 조건을 동적으로 조합할 때 사용
    private Predicate keywordContains(String keyword) {
        if (!hasText(keyword)) {
           return null; // 전체검색
        }
        // 제목, 저자, 출판사명, 본문에서 keyword 검색
        return book.title.containsIgnoreCase(keyword)
                .or(book.author.containsIgnoreCase(keyword))
                .or(book.publisherName.containsIgnoreCase(keyword))
                .or(
                        // QueryDSL -> Hibernate -> SQL
                        Expressions.booleanTemplate(
                                "function('ts_match_korean', {0}, {1}) = true",
                                book.content, keyword
                        )
                );
    }

    private Predicate isbnEq(String isbn) {
        if (!hasText(isbn)) {
            return null;
        }

        return book.isbn13.eq(isbn);
    }
}
