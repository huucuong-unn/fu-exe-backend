package com.exe01.backend.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class CacheService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void deleteKeysContaining(String... patterns) {
        for (String pattern : patterns) {
            Set<String> keysToDelete = redisTemplate.keys("*" + pattern + "*");
            if (keysToDelete != null && !keysToDelete.isEmpty()) {
                redisTemplate.delete(keysToDelete);
            }
        }
    }

}
