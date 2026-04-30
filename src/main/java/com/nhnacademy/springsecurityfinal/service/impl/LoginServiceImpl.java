package com.nhnacademy.springsecurityfinal.service.impl;

import com.nhnacademy.springsecurityfinal.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;


@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String PREFIX = "login:fail:";
    private static final long TTL_MINUTES = 30;

    @Override
    public void fail(String id) {
        String key = PREFIX + id;

        // increment : 키가 없으면 생성 후 1을 반환, 있으면 1을 더한다.
        Long count = redisTemplate.opsForValue().increment(key);

        if (count != null && count == 1) {
            redisTemplate.expire(key, TTL_MINUTES, TimeUnit.MINUTES);
        }
    }

    @Override
    public void login(String id) {
        redisTemplate.delete(PREFIX + id);
    }

    @Override
    public boolean isBlocked(String id) {
        Object o = redisTemplate.opsForValue().get(PREFIX + id);

        if (o == null) {
            return false;
        }

        if (o instanceof Number) {
            return ((Number) o).intValue() >= 5;
        }

        return false;
    }
}
