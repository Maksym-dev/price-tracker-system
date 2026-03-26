package com.mhridin.pts_scraper_service.scraper;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.math.BigDecimal;

@AllArgsConstructor
@Slf4j
public abstract class AbstractJsoupScraping implements ScrapingStrategy {

    private final String domain;

    @Override
    public ScrapeResult fetch(String url) {
        Document doc;
        try {
            doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                    .timeout(5000)
                    .get();

            BigDecimal price = extractPriceFromDocument(doc);
            boolean availableStatus = extractAvailableStatusFromDocument(doc);
            return new ScrapeResult(price, availableStatus);
        } catch (Exception e) {
            log.error("Error scraping Jsoup site {}: {}", url, e.getMessage());
            return new ScrapeResult(new BigDecimal(0), false);
        }
    }

    @Override
    public boolean supports(String url) {
        return url.contains(domain);
    }

    protected abstract BigDecimal extractPriceFromDocument(Document doc);

    protected abstract boolean extractAvailableStatusFromDocument(Document doc);
}
