package com.nhnacademy.flyschedule.dto.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * 공공 데이터 API : 전체 wrapper
 */
public record ApiResponseWrapper<T> (
    @JsonProperty("response")
    Response<T> response
){
    public record Response<T>(
            @JsonProperty("header")
            Header header,

            @JsonProperty("body")
            Body<T> body
    ) {
    }

    public record Header(
            @JsonProperty("resultCode")
            String resultCode,

            @JsonProperty("resultMsg")
            String resultMsg
    ) {
    }

    public record Body<T>(
            @JsonProperty("items")
            Items<T> items,

            @JsonProperty("numOfRows")
            Integer numOfRows,

            @JsonProperty("pageNo")
            Integer pageNo,

            @JsonProperty("totalCount")
            Integer totalCount
    ) {
    }

    public record Items<T>(
            @JsonProperty("item")
            List<T> item
    ) {
    }

    ///  ====== 편의 메서드 ======
    // 성공 여부
    public boolean isSuccess() {
        return response != null && response.header() != null
                && "00".equals(response.header().resultCode());
    }

    // 결과 메시지
    public String getResultMessage() {
        return response != null && response.header != null
                ? response.header().resultMsg : "";
    }

    // 결과 코드
    public String getResultCode() {
        return response != null && response.header != null ? response.header.resultCode : "";
    }

    // item 목록 가져오기
    public List<T> getItems() {
        if(response == null || response.body == null || response.body.items() == null) {
            return List.of();
        }
        return response.body().items().item();
    }

    // totalCount
    public int getTotalCount() {
        if(response == null || response.body() == null) {
            return 0;
        }

        return response.body().totalCount();
    }
}
