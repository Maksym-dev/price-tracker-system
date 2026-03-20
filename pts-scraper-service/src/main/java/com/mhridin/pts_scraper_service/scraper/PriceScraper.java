package com.mhridin.pts_scraper_service.scraper;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Slf4j
public class PriceScraper {
    public BigDecimal scrape(String url) throws Exception {
        Document doc = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                .timeout(5000)
                .get();

        // TODO Make choosing selector by domain
        Element priceElement = doc.selectFirst(".product-price__big"); // CSS-selector for Rozetka domain

        if (priceElement == null) {
            log.info("Price element not found for URL: {}", url);
            return new BigDecimal(0);
        }

        // Clear string
        String priceText = priceElement.text().replaceAll("[^0-9,.]", "").replace(",", ".");
        return new BigDecimal(priceText);
    }
}
