package com.mhridin.pts_scraper_service.scraper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class PriceScraper {

    private final List<ScrapingStrategy> strategies;

    public ScrapeResult scrape(String url) {
        return strategies.stream()
                .filter(s -> s.supports(url))
                .findFirst()
                .orElseThrow(() -> new UnsupportedOperationException("Site " + url + " not supported"))
                .fetch(url);
    }
}
