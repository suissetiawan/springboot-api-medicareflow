package com.dibimbing.medicareflow.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenBlacklistService {

    private final RedisTemplate<String, String> redisTemplate;
    private static final String PREFIX = "Blacklist:";

    public void blacklistToken(String token, long ttlMillis) {
        log.info("Blacklisting token: {} with TTL: {} ms", token, ttlMillis);
        redisTemplate.opsForValue().set(PREFIX + token, "true", ttlMillis, TimeUnit.MILLISECONDS);
    }

    public boolean isTokenBlacklisted(String token) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(PREFIX + token));
    }
}
