package com.nhnacademy.springmvcfinal.common;

import com.nhnacademy.springmvcfinal.exception.ValidationFailedException;
import com.nhnacademy.springmvcfinal.exception.base.BadRequestException;
import com.nhnacademy.springmvcfinal.exception.base.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    // TODO error 출력이 안되고 있는 것 같다 + 코드 정리 필요
    @ExceptionHandler(value = {ValidationFailedException.class, BadRequestException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleBadRequest(Exception ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        model.addAttribute("code", HttpStatus.BAD_REQUEST);
        return "error";
    }

    @ExceptionHandler(value = {NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFound(Exception ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        model.addAttribute("code", HttpStatus.NOT_FOUND);
        return "error";
    }
}
