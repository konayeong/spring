package com.example.core.common.exception.price;

public class NotFoundSectorsException extends RuntimeException {
    public NotFoundSectorsException(String city) {
        super("city:" + city + "에 등록된 업종 목록이 존재하지 않습니다.");
    }
}
