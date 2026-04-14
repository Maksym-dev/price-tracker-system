package com.mhridin.pts_scraper_service.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
@Slf4j
public class RateLimiterService {

    private final StringRedisTemplate redisTemplate;
    private RedisScript<Long> rateLimitScript;

    @PostConstruct
    public void init() {
        rateLimitScript = RedisScript.of(
                new ClassPathResource("scripts/rate_limiter.lua"),
                Long.class
        );
    }

    public boolean isAllowed(String domain, int limit, int windowSeconds) {
        String key = "rate_limit:" + domain;
        long now = System.currentTimeMillis();
        long windowStart = now - (windowSeconds * 1000L);

        try {
            Long result = redisTemplate.execute(
                    rateLimitScript,
                    Collections.singletonList(key),
                    String.valueOf(now),
                    String.valueOf(windowStart),
                    String.valueOf(limit),
                    String.valueOf(windowSeconds)
            );

            return result != null && result == 1;
        } catch (Exception e) {
            log.error("Failed to execute rate limit script for domain {}", domain, e);
            return false;
        }
    }
}
