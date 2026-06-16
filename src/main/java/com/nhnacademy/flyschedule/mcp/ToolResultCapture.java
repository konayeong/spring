package com.nhnacademy.flyschedule.mcp;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import java.util.HashMap;
import java.util.Map;

/**
 * Tool 호출 결과를 캡처
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ToolResultCapture {
    // ThreadLocal : 요청마다 독립된 저장공간 제공
    private static final ThreadLocal<Map<String, Object>> RESULTS = new ThreadLocal<>();

    public static void capture(String toolName, Object result) {
        if(RESULTS.get() == null) {
            RESULTS.set(new HashMap<>());
        }
        RESULTS.get().put(toolName, result);
    }

    // 서비스 계층에서 꺼내 쓰고 자동 삭제
    public static Map<String, Object> getAndClear() {
        Map<String, Object> results = RESULTS.get();
        RESULTS.remove();
        return results != null ? results : new HashMap<>();
    }
}
