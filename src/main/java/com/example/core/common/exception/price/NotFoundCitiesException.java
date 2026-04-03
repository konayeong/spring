package com.example.core.common.exception.price;

public class NotFoundCitiesException extends RuntimeException {
    public NotFoundCitiesException() {
        super("등록된 지자체가 없습니다.");
    }
}
