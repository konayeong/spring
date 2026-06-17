package com.nhnacademy.flyschedule.dto;

import org.springframework.context.annotation.Description;

/**
 * LLM이 추출한 결과를 Entity로 받기 위해 Description 설정
 */
public record FlightSearchParam (
        @Description("출발 공항 또는 도시 이름. 예: 광주, 제주, 김포")
        String departure,

        @Description("도착 공항 또는 도시 이름. 예: 제주, 부산, 김포")
        String arrival,

        @Description("출발 날짜. 사용자가 말한 원본 표현 그대로 추출. 예: 내일, 모레, 2026-06-18")
        String date,

        @Description("이후 출발 시간. HH:mm 형식. 없으면 null")
        String afterTime,

        @Description("이전 출발 시간. HH:mm 형식. 없으면 null")
        String beforeTime,

        @Description("최소 가격. 숫자만 추출. 없으면 null")
        Integer minPrice,

        @Description("최대 가격. 숫자만 추출. 없으면 null")
        Integer maxPrice
) {
}
