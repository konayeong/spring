package com.nhnacademy.ailibraryteam3batch.service.impl;

import com.nhnacademy.ailibraryteam3batch.dto.search.BookSearchRequest;
import com.nhnacademy.ailibraryteam3batch.dto.search.BookSearchResponse;
import com.nhnacademy.ailibraryteam3batch.dto.search.SearchType;
import com.nhnacademy.ailibraryteam3batch.repository.BookRepository;
import com.nhnacademy.ailibraryteam3batch.service.BookSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookSearchServiceImpl implements BookSearchService {
    private final BookRepository bookRepository;

    @Override
    public Page<BookSearchResponse> search(Pageable pageable, BookSearchRequest request) {
        String keyword = request.keyword();

        SearchType searchType = request.searchType();
        if(searchType == null) {
            return bookRepository.searchAll(pageable);
        }

        return switch (searchType) {
            case KEYWORD -> bookRepository.searchByKeyword(pageable, keyword);
            case ISBN -> bookRepository.searchByIsbn(pageable, request.isbn());
            case VECTOR -> null;
            case HYBRID -> null;
        };
    }
}
