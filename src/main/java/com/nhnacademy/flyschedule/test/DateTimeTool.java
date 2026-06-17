package com.nhnacademy.flyschedule.test;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Step3-6. 복잡한 Tool 구현 실습
 * - 날짜 계산 : 내일, 모레 같은 상대적 날짜를 실제 날짜로 변환
 */
@Component
public class DateTimeTool {
    // mm : 분, DD : 1년 중 몇 번째 날
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");

    @Tool(description = """
            상대적 날짜를 실제 날짜로 변환하여 반환합니다.
            '내일','모레','글피','10일뒤' 등을 지원합니다.
            
            사용 예시:
            - 내일 날짜 알려줘
            - 4일 뒤 날짜 알려줘
            
            파라미터:
            - date: 상대적 날짜 표현 (내일, 모레, 글피, 10일 뒤)
            
            반환값: 실제 날짜 (yyyy년 MM월 dd일)
            """)
    public String parseDate(@ToolParam(description = "상대적 날짜 표현(예: 내일, 모레, 글피, 10일 뒤)") String date) {
        LocalDate today = LocalDate.now();

        return switch (date.trim()) {
            case "오늘" -> today.format(DATE_FORMATTER);
            case "내일" -> today.plusDays(1).format(DATE_FORMATTER);
            case "모레" -> today.plusDays(2).format(DATE_FORMATTER);
            case "글피" -> today.plusDays(3).format(DATE_FORMATTER);
            default -> {
                Matcher matcher = Pattern.compile("(\\d+)일\\s+뒤").matcher(date);

                if(matcher.matches()) {
                    int days = Integer.parseInt(matcher.group(1));
                    yield today.plusDays(days).format(DATE_FORMATTER);
                }
                throw new IllegalArgumentException("지원하지 않는 날짜 표현: " + date);
            }
        };
    }
}
