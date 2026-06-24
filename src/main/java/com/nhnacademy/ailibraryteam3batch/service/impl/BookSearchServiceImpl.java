package com.nhnacademy.ailibraryteam3batch.service.impl;

import com.nhnacademy.ailibraryteam3batch.dto.search.BookSearchResponse;
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
    public Page<BookSearchResponse> searchBooks(Pageable pageable, String keyword) {
        return bookRepository.searchByKeyword(pageable, keyword);
    }

    @Override
    public Page<BookSearchResponse> searchBooksByISBN(Pageable pageable, String isbn) {
        return bookRepository.searchByIsbn(pageable, isbn);
    }
}
