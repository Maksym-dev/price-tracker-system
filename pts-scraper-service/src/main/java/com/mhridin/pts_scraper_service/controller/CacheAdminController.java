package com.mhridin.pts_scraper_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/cache")
@RequiredArgsConstructor
public class CacheAdminController {

    private final CacheManager cacheManager;

    @PostMapping("/clear-domains")
    public ResponseEntity<String> clearCache() {
        Cache cache = cacheManager.getCache("domainConfigs");
        if (cache != null) {
            cache.clear();
            return ResponseEntity.ok("Cache cleared");
        }
        return ResponseEntity.status(500).body("Cache not found");
    }
}
