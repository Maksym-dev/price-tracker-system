package com.mhridin.pts_scraper_service.consumer;

import com.mhridin.pts_common.kafka.PriceCheckEvent;
import com.mhridin.pts_scraper_service.scraper.PriceScraper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
@RequiredArgsConstructor
public class PriceCheckScraperConsumer {

    private final PriceScraper priceScraper;

    @KafkaListener(topics = "price-check-tasks", groupId = "scraper-group")
    public void consumePriceCheck(PriceCheckEvent event) {
        log.info("Received task for product: {}", event.getProductId());

        try {
            BigDecimal price = priceScraper.scrape(event.getUrl());

            if (price.equals(BigDecimal.ZERO)) {
                log.info("Price was not found for URL: {}", event.getUrl());
                return;
            }

            log.info("Successfully scraped price for product {}: {}", event.getProductId(), price);

        } catch (Exception e) {
            log.error("Failed to scrape product {}: {}", event.getProductId(), e.getMessage());
        }
    }
}
