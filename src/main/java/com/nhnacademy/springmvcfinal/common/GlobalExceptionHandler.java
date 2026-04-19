package com.nhnacademy.springmvcfinal.common;

import com.nhnacademy.springmvcfinal.exception.ValidationFailedException;
import com.nhnacademy.springmvcfinal.exception.base.BadRequestException;
import com.nhnacademy.springmvcfinal.exception.base.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

// TODO-E : 오류 출력 안되는중
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({ValidationFailedException.class, BadRequestException.class})
    public String handleBadRequest(Exception ex, Model model) {
        return handleException(ex, model, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public String handleNotFound(Exception ex, Model model) {
        return handleException(ex, model, HttpStatus.NOT_FOUND);
    }

    private String handleException(Exception ex, Model model, HttpStatus status) {
        model.addAttribute("error", ex.getMessage() != null ? ex.getMessage() : defaultMessage(status));
        model.addAttribute("code", status.value());
        return "error";
    }

    private String defaultMessage(HttpStatus status) {
        return switch (status) {
            case BAD_REQUEST -> "잘못된 요청입니다.";
            case NOT_FOUND -> "요청한 리소스를 찾을 수 없습니다.";
            default -> "서버 오류가 발생했습니다.";
        };
    }
}