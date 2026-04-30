package com.nhnacademy.springsecurityfinal.repository;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Objects;
import java.util.UUID;

/**
 * SecurityContext 를 Redis 에 저장하는 SecurityContextRepository.
 * HttpSession 대신 직접 발급한 쿠키(ACADEMY-SESSION) 의 UUID 를 키로 사용 -> 스케일아웃 가능.
 */
@Component
public class RedisSecurityContextRepository implements SecurityContextRepository {

    private static final String COOKIE_NAME = "ACADEMY-SESSION";
    private static final String REDIS_KEY_PREFIX = "security:context:";
    private static final Duration EXPIRE = Duration.ofMinutes(30);

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisSecurityContextRepository(RedisConnectionFactory connectionFactory) {
        this.redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
        redisTemplate.afterPropertiesSet();
    }

    // 매 요청마다 SecurityContextHolderFilter 가 호출 -> Redis 에서 SecurityContext 복원
    @Override
    public SecurityContext loadContext(HttpRequestResponseHolder holder) {
        String sessionId = readCookie(holder.getRequest());
        if (sessionId == null) {
            return SecurityContextHolder.createEmptyContext();
        }

        Object o = redisTemplate.opsForValue().get(REDIS_KEY_PREFIX + sessionId);
        if(Objects.nonNull(o) && o instanceof SecurityContext context) {
            return context;
        }
        return SecurityContextHolder.createEmptyContext();
    }

    // 로그인 성공 시 successfulAuthentication() 에서 호출 -> 쿠키 발급 + Redis 저장
    @Override
    public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
        String sessionId = readCookie(request);
        if (sessionId == null) {
            sessionId = UUID.randomUUID().toString();
            writeCookie(response, sessionId);
        }

        redisTemplate.opsForValue().set(REDIS_KEY_PREFIX + sessionId, context, EXPIRE);
    }

    @Override
    public boolean containsContext(HttpServletRequest request) {
        String sessionId = readCookie(request);
        if (sessionId == null) {
            return false;
        }
        return Boolean.TRUE.equals(redisTemplate.hasKey(REDIS_KEY_PREFIX + sessionId));
    }

    private String readCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            return null;
        }

        for (Cookie cookie : cookies) {
            if (COOKIE_NAME.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    private void writeCookie(HttpServletResponse response, String sessionId) {
        Cookie cookie = new Cookie(COOKIE_NAME, sessionId);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

    public void deleteContext(String sessionId) {
        redisTemplate.delete(REDIS_KEY_PREFIX + sessionId);
    }
}