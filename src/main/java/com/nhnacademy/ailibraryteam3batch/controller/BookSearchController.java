package com.nhnacademy.ailibraryteam3batch.controller;

import com.nhnacademy.ailibraryteam3batch.dto.search.BookSearchRequest;
import com.nhnacademy.ailibraryteam3batch.dto.search.BookSearchResponse;
import com.nhnacademy.ailibraryteam3batch.dto.search.PageResponse;
import com.nhnacademy.ailibraryteam3batch.dto.search.SearchType;
import com.nhnacademy.ailibraryteam3batch.service.BookSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BookSearchController {
    private final BookSearchService bookSearchService;

    @GetMapping("/")
    public String search(@ModelAttribute BookSearchRequest request,
                         @PageableDefault(size = 10) Pageable pageable,
                         Model model) {
        long start = System.currentTimeMillis();
        Page<BookSearchResponse> bookSearchResponses;
        SearchType searchType = request.searchType();

        // isbn 검색
        if(searchType != null && searchType.equals(SearchType.ISBN)) {
            log.info("ISBN 검색 요청 : {}", request.isbn());
            bookSearchResponses = bookSearchService.searchBooksByISBN(pageable, request.isbn());
        } else {
            log.info("키워드 검색 요청 : {}", searchType == null ? "ALL" : request.keyword());
            bookSearchResponses = bookSearchService.searchBooks(pageable, request.keyword());
        }
        PageResponse<BookSearchResponse> result = PageResponse.from(bookSearchResponses);

        long searchTime = System.currentTimeMillis() - start; // ms
        model.addAttribute("books", result.content());
        model.addAttribute("page", result);
        model.addAttribute("request", request);
        model.addAttribute("searchTime", searchTime / 1000.0);
        return "index";
    }

}
