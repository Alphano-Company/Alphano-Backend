package com.alphano.alphano.common.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class RedisService {
    private final StringRedisTemplate redis;

    public void set(String key, String value, Duration ttl) {
        redis.opsForValue().set(key, value, ttl);
    }

    public String get(String key) {
        return redis.opsForValue().get(key);
    }

    public void delete(String key) {
        redis.delete(key);
    }

    public long incrementWithTtl(String key, Duration ttl) {
        Long after = redis.opsForValue().increment(key);
        if (after != null && after == 1L) {
            redis.expire(key, ttl);
        }
        return after == null ? 0L : after;
    }
}
