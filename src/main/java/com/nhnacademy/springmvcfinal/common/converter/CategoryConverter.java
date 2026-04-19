package com.nhnacademy.springmvcfinal.common.converter;

import com.nhnacademy.springmvcfinal.domain.inquiry.Category;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CategoryConverter implements Converter<String, Category> {
    @Override
    public Category convert(String source) { // DTO에 값을 넣기 전에 실행
        return Category.from(Integer.parseInt(source));
    }
}
