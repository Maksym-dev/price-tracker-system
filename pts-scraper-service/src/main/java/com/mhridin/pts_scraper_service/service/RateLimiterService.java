package com.mhridin.pts_scraper_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RateLimiterService {

    private final StringRedisTemplate redisTemplate;

    public boolean isAllowed(String domain, int limit, int windowSeconds) {
        String key = "rate_limit:" + domain;
        long now = System.currentTimeMillis();
        long windowStart = now - (windowSeconds * 1000L);

        // Use a Redis transaction (Pipeline) to ensure atomic operations
        List<Object> results = redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            // 1. Remove labels that have fallen outside the current window
            connection.zSetCommands().zRemRangeByScore(key.getBytes(), 0, windowStart);
            // 2. Count how many labels remain (how many requests were in the window)
            connection.zSetCommands().zCard(key.getBytes());
            // 3. Add the current label
            connection.zAdd(key.getBytes(), now, String.valueOf(now).getBytes());
            // 4. Set the TTL for the key so it doesn't hang around in memory forever
            connection.keyCommands().expire(key.getBytes(), windowSeconds);
            return null;
        });

        // The zCard result is at index 1 in the pipeline result list.
        Long currentCount = (Long) results.get(1);

        return currentCount != null && currentCount < limit;
    }
}
