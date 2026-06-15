package com.nhnacademy.flyschedule.agent;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 공항 이름 -> 공항 코드
 */
@Slf4j
@Service
public class AirportCodeAgent {
    private static final Map<String, String> AIRPORT_CODE_MAP = new HashMap<>();

    // 클래스가 로드될 때 한번 실행되는 초기화 블록
    static {
        // 수도권
        AIRPORT_CODE_MAP.put("김포", "NAARKSS");
        AIRPORT_CODE_MAP.put("인천", "NAARKSI");

        // 부산/경남권
        AIRPORT_CODE_MAP.put("김해", "NAARKJB");
        AIRPORT_CODE_MAP.put("부산", "NAARKJB");
        AIRPORT_CODE_MAP.put("사천", "NAARKPS");
        AIRPORT_CODE_MAP.put("울산", "NAARKNU");

        // 호남권
        AIRPORT_CODE_MAP.put("광주", "NAARKJJ");
        AIRPORT_CODE_MAP.put("여수", "NAARKJY");
        AIRPORT_CODE_MAP.put("무안", "NAARKJB");

        // 영남권
        AIRPORT_CODE_MAP.put("대구", "NAARKTN");
        AIRPORT_CODE_MAP.put("포항", "NAARKPK");

        // 충청/강원권
        AIRPORT_CODE_MAP.put("청주", "NAARKNJ");
        AIRPORT_CODE_MAP.put("양양", "NAARKNY");

        // 제주권
        AIRPORT_CODE_MAP.put("제주", "NAARKPC");
    }

    public String getAirportCode(String airportName) {
        if(airportName == null || airportName.isBlank()) {
            throw new IllegalArgumentException("공항 이름을 입력해주세요.");
        }

        String normalized = airportName
                .trim()
                .replace("공항", "")
                .replace(" ", "");

        // 공항 코드를 입력하면 그대로 반환
        if(normalized.matches("NAARK[A-Z]{2}")) {
            return normalized;
        }

        String code = AIRPORT_CODE_MAP.get(normalized);
        if(code == null) {
            throw new IllegalArgumentException("존재하지 않는 공항입니다: " + airportName);
        }

        return code;
    }

    public boolean isValidAirport(String airportName) {
        if(airportName == null || airportName.isBlank()) {
            return false;
        }
        return AIRPORT_CODE_MAP.containsKey(airportName);
    }
}
