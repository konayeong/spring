package com.nhnacademy.flyschedule.service;

import com.nhnacademy.flyschedule.config.ApiProperties;
import com.nhnacademy.flyschedule.dto.AirlineInfoResponse;
import com.nhnacademy.flyschedule.dto.AirportInfoResponse;
import com.nhnacademy.flyschedule.dto.ApiResponseWrapper;
import com.nhnacademy.flyschedule.dto.FlightInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriBuilder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

/**
 * 항공 API와 통신
 * - 기능이 적기 때문에 service 분리는 하지 않음
 * - Repository, 외부 데이터 소스 접근
 */
// TODO-Q @Component아니고 @Service로 설정 이유는 ?
@Slf4j
@Service
@RequiredArgsConstructor
public class FlightApiClient {
    private final ApiProperties apiProperties;
    @Qualifier("dataGoKrRestClient")
    private final RestClient restClient;

    /**
     * 항공편 조회
     * @param depAirportId : 출발 공항 아이디
     * @param arrAirportId : 도착 공항 아이디
     * @param date : 출발일
     * @return : List<항공편>
     */
    public List<FlightInfoResponse> getFlightSchedule(String depAirportId, String arrAirportId, String date) {
        // TODO Exception
        ApiResponseWrapper<FlightInfoResponse> wrapper = restClient.get()
                .uri(uriBuilder ->
                        addCommonParam(uriBuilder)
                                .path("/GetFlightOpratInfoList")
                                .queryParam("depAirportId", encodeParam(depAirportId))
                                .queryParam("arrAirportId", encodeParam(arrAirportId))
                                .queryParam("depPlandTime", encodeParam(date))
                                .build()).retrieve()
                .body(new ParameterizedTypeReference<>() {
                });

        if (wrapper.isSuccess()) {
            log.info("항공편 {}건 조회 완료", wrapper.getTotalCount());
            return wrapper.getItems();

        }

        log.error("항공편 조회 에러 : {} - {}", wrapper.getResultCode(), wrapper.getResultMessage());
        return Collections.emptyList();
    }

    /**
     * 공항 목록 조회
     */
    public List<AirportInfoResponse> getAirportList() {
        ApiResponseWrapper<AirportInfoResponse> wrapper = restClient.get()
                .uri(uriBuilder ->
                        addCommonParam(uriBuilder)
                                .path("/GetArprtList")
                                .build()).retrieve()
                .body(new ParameterizedTypeReference<ApiResponseWrapper<AirportInfoResponse>>() {});

        if(wrapper.isSuccess()) {
            return wrapper.getItems();
        }

        log.error("공항 목록 조회 에러 : {} - {}", wrapper.getResultCode(), wrapper.getResultMessage());
        return Collections.emptyList();
    }

    /**
     * 항공사 목록 조회
     */
    public List<AirlineInfoResponse> getAirlineList() {
        ApiResponseWrapper<AirlineInfoResponse> wrapper = restClient.get()
                .uri(uriBuilder ->
                        addCommonParam(uriBuilder)
                                .path("/GetAirmanList")
                                .build()).retrieve()
                .body(new ParameterizedTypeReference<ApiResponseWrapper<AirlineInfoResponse>>() {});

        if(wrapper.isSuccess()) {
            return wrapper.getItems();
        }
        log.error("항공사 목록 조회 에러 : {} - {}", wrapper.getResultCode(), wrapper.getResultMessage());
        return Collections.emptyList();
    }

    /**
     * Request 공통 파라미터
     */
    private UriBuilder addCommonParam(UriBuilder uriBuilder) {
        return uriBuilder.queryParam("serviceKey", apiProperties.getServiceKey())
                .queryParam("_type", "json");
    }

    /**
     * 파라미터 URL 인코딩
     */
    private String encodeParam(String param) {
        try {
            return URLEncoder.encode(param, StandardCharsets.UTF_8);
        }catch (Exception e) {
            log.warn("파라미터 인코딩 실패 : {}", param);
            return param;
        }
    }
}
