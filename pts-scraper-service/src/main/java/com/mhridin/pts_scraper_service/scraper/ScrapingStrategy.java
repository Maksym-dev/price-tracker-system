package com.mhridin.pts_scraper_service.scraper;

public interface ScrapingStrategy {
    ScrapeResult fetch(String url);

    boolean supports(String url);
}
