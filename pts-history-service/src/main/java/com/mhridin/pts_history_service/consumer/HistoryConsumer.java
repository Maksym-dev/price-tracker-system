package com.mhridin.pts_history_service.consumer;

import com.mhridin.pts_common.kafka.PriceUpdateEvent;
import com.mhridin.pts_history_service.mongodb.entity.PriceHistory;
import com.mhridin.pts_history_service.mongodb.repository.PriceHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class HistoryConsumer {

    private final PriceHistoryRepository priceHistoryRepository;

    @KafkaListener(topics = "price-update-events", groupId = "history-group")
    public void consumePriceUpdate(PriceUpdateEvent event) {
        if (!"SUCCESS".equals(event.getStatus())) {
            log.info("Failed saving price history for product {}", event.getProductId());
            return;
        }

        PriceHistory historyEntry = PriceHistory.builder()
                .productId(event.getProductId())
                .price(event.getNewPrice())
                .timestamp(LocalDateTime.now())
                .build();

        priceHistoryRepository.save(historyEntry);
        log.info("Saved price history for product {}", event.getProductId());
    }
}
