package com.nhnacademy.ailibraryteam3batch.dto;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVRecord;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
public record BookRawData(
        Long id,                // 일련번호
        String isbn,    // ISBN 13자리
        String volumnName,              // 권 명
        String title,            // 도서명
        String author,            // 저자명
        String publisherName,        // 출판사명
        LocalDate firstPublishDate,       // 발행일
        String additionSymbol,      // 부가기호명
        Integer price,       // 가격
        String image,           // 이미지 URL
        String content,      // 도서 소개 내용
        String kdc,              // 한국십진분류명
        String subTitle,       // 대체 도서명
        String subAuthor,       // 대체 저자명
        LocalDate secondPublishDate,   // 2차 발행일
        Boolean isBookStorePresent,  // 인터넷 서점 도서 존재 여부
        Boolean isPortalPresent,   // 포털 사이트 도서 존재 여부
        Long isbn10              // 구 ISBN
        ) {

    public static BookRawData from(CSVRecord record) {
        return new BookRawData(
                getLong(record, "SEQ_NO"),
                record.get("ISBN_THIRTEEN_NO"),
                record.get("VLM_NM"),
                record.get("TITLE_NM"),
                record.get("AUTHR_NM"),
                record.get("PUBLISHER_NM"),
                getDate(record, "PBLICTE_DE"),
                record.get("ADTION_SMBL_NM"),
                getInteger(record, "PRC_VALUE"),
                record.get("IMAGE_URL"),
                record.get("BOOK_INTRCN_CN"),
                record.get("KDC_NM"),
                record.get("TITLE_SBST_NM"),
                record.get("AUTHR_SBST_NM"),
                getDate(record, "TWO_PBLICTE_DE"),
                getBoolean(record, "INTNT_BOOKST_BOOK_EXST_AT"),
                getBoolean(record, "PORTAL_SITE_BOOK_EXST_AT"),
                null
        );
    }

    private static Long getLong(CSVRecord record, String column) {
        String value = record.get(column);

        if (value == null || value.isBlank()) {
            return null;
        }

        try {
            String number = value.replaceAll("[^0-9]", "");

            if (number.isBlank()) {
                return null;
            }

            return Long.parseLong(number);

        } catch (Exception e) {
            log.warn("Long 파싱 실패 - column={}, value={}", column, value);
            return null;
        }
    }

    private static Integer getInteger(CSVRecord record, String column) {
        String value = record.get(column);

        if (value == null || value.isBlank()) {
            return null;
        }

        try {
            return new BigDecimal(value).intValue();

        } catch (Exception e) {
            log.warn("정수 파싱 실패 - column={}, value={}", column, value);
            return null;
        }
    }

    private static LocalDate getDate(CSVRecord record, String column) {
        String value = record.get(column);

        if (value == null || value.isBlank()) {
            return null;
        }

        try {
            if (value.contains("-")) {
                return LocalDate.parse(value, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            }

            if (value.length() == 8) {
                return LocalDate.parse(value, DateTimeFormatter.ofPattern("yyyyMMdd"));
            }
            // count로 처리하는게 좋다. (log 너무 많아짐)
            log.warn("잘못된 날짜 형식 - column={}, value={}", column, value);
            return null;

        } catch (Exception e) {
            log.warn("날짜 파싱 실패 - column={}, value={}", column, value);
            return null;
        }
    }

    private static Boolean getBoolean(CSVRecord record, String column) {
        String value = record.get(column);

        if (value == null || value.isBlank()) {
            return null;
        }

        return "Y".equalsIgnoreCase(value);
    }
}
