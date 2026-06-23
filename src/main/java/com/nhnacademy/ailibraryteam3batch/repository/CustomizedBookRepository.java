package com.nhnacademy.ailibraryteam3batch.repository;

import com.nhnacademy.ailibraryteam3batch.dto.search.BookSearchRequest;
import com.nhnacademy.ailibraryteam3batch.dto.search.BookSearchResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * QueryDSL을 사용한 복잡한 검색 기능 정의
 */
public interface CustomizedBookRepository {
    Page<BookSearchResponse> search(Pageable pageable, BookSearchRequest request);
}
