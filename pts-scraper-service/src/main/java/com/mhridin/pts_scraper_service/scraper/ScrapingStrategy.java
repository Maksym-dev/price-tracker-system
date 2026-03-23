package com.mhridin.pts_scraper_service.scraper;

import java.math.BigDecimal;

public interface ScrapingStrategy {
    BigDecimal fetchPrice(String url);

    boolean supports(String url);
}
