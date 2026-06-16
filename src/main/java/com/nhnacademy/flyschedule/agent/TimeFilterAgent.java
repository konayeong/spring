package com.nhnacademy.flyschedule.agent;

import com.nhnacademy.flyschedule.dto.FlightInfoResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 출발 시간을 기준으로 항공편 필터링
 */
@Slf4j
@Service
public class TimeFilterAgent {
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HHmm");

    /**
     * 시간 문자열 파싱
     * @param timeInput : 시간 문자열
     * @return LocalTime 객체
     */
    public LocalTime parseTime(String timeInput) {
        log.info("[TimeFilterAgent] 시간 문자열 파싱 시작");
        if(timeInput == null || timeInput.isBlank()) {
            throw new IllegalArgumentException("시간을 입력해주세요.");
        }

        String normalized = timeInput.trim();

        // "오후 2시" 형식 처리
        if (normalized.contains("오후")) {
            String numbersOnly = normalized.replaceAll("[^0-9]", "");
            if (!numbersOnly.isEmpty()) {
                int hour = Integer.parseInt(numbersOnly);
                if (hour < 12) hour += 12; // 2시 -> 14시
                if (hour >= 24) hour = 12;
                return LocalTime.of(hour, 0);
            }
        }

        // "오전 9시" 형식 처리
        if (normalized.contains("오전")) {
            String numbersOnly = normalized.replaceAll("[^0-9]", "");
            if (!numbersOnly.isEmpty()) {
                int hour = Integer.parseInt(numbersOnly);
                if (hour == 12) hour = 0;
                return LocalTime.of(hour, 0);
            }
        }

        // "HHmm" 형식 처리
        String cleaned = normalized.replace(":", "");
        return LocalTime.parse(cleaned, TIME_FORMATTER);
    }

    /**
     * 기준 시간 이후에 출발하는 항공편만 필터링
     * @param flights : 항공편
     * @param afterTime : 기준 시간
     */
    public List<FlightInfoResponse> filterAfterTime(List<FlightInfoResponse> flights, LocalTime afterTime) {
        log.info("[TimeFilterAgent] {} 이후에 출발하는 항공편 필터링 시작", afterTime);
        if (flights == null || flights.isEmpty()) {
            log.warn("[TimeFilterAgent] 항공편이 비어있다.");
            return List.of();
        }

        return flights.stream()
                .filter(flight -> {
                    try {
                        // depPlandTime은 "202503090955" 형식 → 시간 부분(0955) 추출
                        String timePart = flight.depPlandTime().substring(8, 12);
                        LocalTime departureTime = LocalTime.parse(timePart, TIME_FORMATTER);
                        return !departureTime.isBefore(afterTime);
                    } catch (Exception e) {
                        log.warn("시간 파싱 실패: {}", flight.depPlandTime());
                        return false;
                    }
                })
                .collect(Collectors.toList());
    }
}
