package com.nhnacademy.springmvcfinal.config;

import com.nhnacademy.springmvcfinal.common.converter.CategoryConverter;
import com.nhnacademy.springmvcfinal.common.interceptor.AdminCheckInterceptor;
import com.nhnacademy.springmvcfinal.common.interceptor.LoginCheckInterceptor;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new CategoryConverter());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(new LoginCheckInterceptor())
                .addPathPatterns("/cs/**")
                .excludePathPatterns(
                        "/cs/login",
                        "/cs/logout",
                        "/error"
                );
        registry.addInterceptor(new AdminCheckInterceptor())
                .addPathPatterns("/cs/admin/**");

        registry.addInterceptor(new LocaleChangeInterceptor());
    }

    // 브라우저 밖에 저장한 파일을 브라우저에서 접근 가능하게 함
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + System.getProperty("user.dir") + "/uploads/");
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        // 기본 파라미터 locale -> lang으로 바꾸는 과정
        LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
        interceptor.setParamName("lang");
        return interceptor;
    }

    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver resolver = new SessionLocaleResolver();
        resolver.setDefaultLocale(Locale.KOREAN); // 기본 locale
        return resolver;
    }

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setBasename("messages");
        return messageSource;
    }
}
