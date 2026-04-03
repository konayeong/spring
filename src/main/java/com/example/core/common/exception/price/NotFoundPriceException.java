package com.example.core.common.exception.price;

public class NotFoundPriceException extends RuntimeException {
    public NotFoundPriceException(String city, String sector) {
        super(String.format("city:%s, sector:%s 구간금액 정보가 없습니다.", city, sector));
    }
}
