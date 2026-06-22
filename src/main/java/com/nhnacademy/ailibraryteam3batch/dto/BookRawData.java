package com.nhnacademy.ailibraryteam3batch.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BookRawData(
        Long SEQ_NO,                // 일련번호
        String ISBN_THIRTEEN_NO,    // ISBN 13자리
        String VLM_NM,              // 권 명
        String TITLE_NM,            // 도서명
        String AUTHR_NM,            // 저자명
        String PUBLISHER_NM,        // 출판사명
        LocalDate PBLICTE_DE,       // 발행일
        String ADTION_SMBL_NM,      // 부가기호명
        BigDecimal PRC_VALUE,       // 가격
        String IMAGE_URL,           // 이미지 URL
        String BOOK_INTRCN_CN,      // 도서 소개 내용
        String KDC_NM,              // 한국십진분류명
        String TITLE_SBST_NM,       // 대체 도서명
        String AUTHR_SBST_NM,       // 대체 저자명
        LocalDate TWO_PBLICTE_DE,   // 2차 발행일
        Boolean INTNT_BOOKST_BOOK_EXST_AT,  // 인터넷 서점 도서 존재 여부
        Boolean PORTAL_SITE_BOOK_EXST_AT,   // 포털 사이트 도서 존재 여부
        Long ISBN_NO                // 구 ISBN
        ) {
}
