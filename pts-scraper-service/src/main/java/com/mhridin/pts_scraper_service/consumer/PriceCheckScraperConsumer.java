package com.mhridin.pts_scraper_service.consumer;

import com.mhridin.pts_common.kafka.PriceCheckEvent;
import com.mhridin.pts_common.kafka.PriceUpdateEvent;
import com.mhridin.pts_scraper_service.scraper.PriceScraper;
import com.mhridin.pts_scraper_service.scraper.ScrapeResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
@RequiredArgsConstructor
public class PriceCheckScraperConsumer {

    private final PriceScraper priceScraper;
    private final KafkaTemplate<String, PriceUpdateEvent> kafkaTemplate;

    @Value("${app.kafka.topics.price-update}")
    private String updateTopic;

    @KafkaListener(topics = "price-check-tasks", groupId = "scraper-group")
    public void consumePriceCheck(PriceCheckEvent event) {
        log.info("Received task for product: {}", event.getProductId());

        try {
            ScrapeResult result = priceScraper.scrape(event.getUrl());

            if (result.getPrice().equals(BigDecimal.ZERO)) {
                log.info("Price was not found for URL: {}", event.getUrl());
                return;
            }

            PriceUpdateEvent updateEvent = new PriceUpdateEvent(
                    event.getProductId(),
                    result.getPrice(),
                    result.isAvailableStatus(),
                    "SUCCESS"
            );

            kafkaTemplate.send(updateTopic, event.getProductId().toString(), updateEvent);
            log.info("Successfully scraped price for product {}: {}", event.getProductId(), result.getPrice());

        } catch (Exception e) {
            log.error("Failed to scrape product {}: {}", event.getProductId(), e.getMessage());

            kafkaTemplate.send(updateTopic, event.getProductId().toString(),
                    new PriceUpdateEvent(event.getProductId(), null, false, "FAILED"));
        }
    }
}
