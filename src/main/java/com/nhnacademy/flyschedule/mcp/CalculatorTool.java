package com.nhnacademy.flyschedule.mcp;

import com.nhnacademy.flyschedule.tool.AiTool;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

/**
 * Step3-1 : @Tool 학습을 위한 실습
 */
@Component
@RequiredArgsConstructor
public class CalculatorTool implements AiTool {

    /**
     * 두 숫자로 더하기, 빼기, 곱하기
     * - LLM은 @Tool의 description을 보고 언제 호출할지 결정
     */
    @Tool(description = "두 숫자의 합을 출력합니다.")
    public int add(@ToolParam(description = "첫 번째 숫자") int first,
                   @ToolParam(description = "두 번째 숫자") int second) {
        return first + second;
    }

    @Tool(description = "두 숫자의 곱을 출력합니다.")
    public int multiply(@ToolParam(description = "첫 번째 숫자") int first,
                        @ToolParam(description = "두 번째 숫자") int second) {
        return first * second;
    }

    @Tool(description = "두 숫자의 차를 출력합니다.")
    public int sub(@ToolParam(description = "두 숫자 중 큰 숫자") int first,
                   @ToolParam(description = "두 숫자 중 작은 숫자") int second) {
        return first - second;
    }
}

