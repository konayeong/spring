package com.nhnacademy.flyschedule.tool;

import com.nhnacademy.flyschedule.agent.AirportListAgent;
import com.nhnacademy.flyschedule.dto.AirportInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 * 공항 정보 MCP Tool
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AirportInfoTool implements AiTool{
    private final AirportListAgent airportListAgent;

    /**
     * 공항 전체 목록 조회
     */
    @Tool(
            description = """
            국내 공항을 모두 조회하여 반환합니다.
            
            언제 사용:
            - 사용자가 공항 전체 목록을 조회할 때
            - '국내에 무슨 공항이 있어?' '공항 목록' 등
            
            반환값:
            - List<공항정보> 형태
            """)
    public List<AirportInfoResponse> getAllAirportInfo() {
        log.info("[공항 목록 조회 Tool 호출]");
        return airportListAgent.getAirportList();
    }

    /**
     * 공항 이름으로 코드 조회하기
     */
    @Tool(
            description = """
                    공항 이름을 받아서 공항 코드를 반환합니다.
                    
                    언제 사용:
                    - 사용자가 공항 코드를 검색할 때
                    - 공항 이름으로 코드를 조회할 때
                    - 예: 광주공항코드알려줘, 제주 공항 코드 알려줘
                    
                    파라미터:
                    - airport : 공항 이름
                    
                    반환값:
                    - String : 공항 코드
                    """
    )
    public String getAirportCodeByName(@ToolParam(description = "공항 이름(예: 광주, 부산, 제주 등)") String airPort) {
        log.info("[공항 코드 조회 Tool 호출] 요청 : {}", airPort);
        return airportListAgent.getAirportCode(airPort);
    }
}
