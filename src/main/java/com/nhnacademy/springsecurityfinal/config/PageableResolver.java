package com.nhnacademy.springsecurityfinal.config;

import org.jspecify.annotations.Nullable;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class PageableResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(Pageable.class);
    }

    @Override
    public @Nullable Object resolveArgument(MethodParameter parameter, @Nullable ModelAndViewContainer mavContainer, NativeWebRequest webRequest, @Nullable WebDataBinderFactory binderFactory) throws Exception {
        String pageStr = webRequest.getParameter("page");
        String sizeStr = webRequest.getParameter("size");

        int page = pageStr == null ? 0 : Integer.parseInt(pageStr);
        int size = sizeStr == null ? 5 : Integer.parseInt(sizeStr);

        if(size > 10) {
            size = 10;
        }

        return PageRequest.of(page, size);
    }
}
