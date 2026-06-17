package com.nhnacademy.flyschedule.tool;

import com.nhnacademy.flyschedule.agent.AirlineListAgent;
import com.nhnacademy.flyschedule.dto.AirlineInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AirlineInfoTool {
    private final AirlineListAgent airlineListAgent;

    @Tool(
            description = """
                    국내 항공사를 모두 조회하여 반환합니다.
                    
                    언제 사용:
                    - 사용자가 국내 항공사 전체 목록을 조회할 때
                    - '국내 항공사가 어떤게 있어? ' 항공사 목록' 등
                    
                    반환값:
                    - List<항공사정보> 형태
                    """
    )
    public List<AirlineInfoResponse> getAirlineInfo() {
        log.info("[국내 항공사 조회 Tool 호출]");
        return airlineListAgent.getAirlineList();
    }

    @Tool(
            description = """
                    항공사 이름을 받아서 항공사 아이디를 반환합니다.
                    
                    언제 사용:
                    - 사용자가 항공사 이름으로 아이디를 조회할 때
                    - 사용자가 항공사 아이디를 검색할 때
                    - 예: 제주 항공 아이디?, 대한항공 아이디가 뭐야?
                    
                    반환값:
                    - String : 항공사 아이디
                    """
    )
    public String getAirlineName(@ToolParam(description = "항공사 이름 (예: 아시아나 항공, 대한 항공 등)") String airline) {
        log.info("[항공사 아이디 조회 Tool 호출] 요청 : {}", airline);
        return airlineListAgent.getAirlineId(airline);
    }
}
