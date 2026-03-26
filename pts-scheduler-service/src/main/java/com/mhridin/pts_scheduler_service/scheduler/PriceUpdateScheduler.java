package com.mhridin.pts_scheduler_service.scheduler;

import com.mhridin.pts_common.entity.Product;
import com.mhridin.pts_common.kafka.PriceCheckEvent;
import com.mhridin.pts_common.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class PriceUpdateScheduler {

    private final ProductRepository productRepository;
    private final KafkaTemplate<String, PriceCheckEvent> kafkaTemplate;

    private static final int BATCH_SIZE = 500;

    @Value("${app.kafka.topics.price-check}")
    private String priceCheckTopic;

    @Scheduled(fixedRateString = "${app.scheduling.interval-ms:3600000}")
    @SchedulerLock(name = "PriceUpdateScheduler_schedulePriceChecks", lockAtMostFor = "15m", lockAtLeastFor = "1m")
    @Transactional(readOnly = true)
    public void schedulePriceChecks() {
        log.info("Starting batched price check...");

        LocalDateTime threshold = LocalDateTime.now().minusHours(1);
        int pageNumber = 0;
        Slice<Product> productSlice;

        do {
            Pageable pageable = PageRequest.of(pageNumber, BATCH_SIZE, Sort.by("id"));
            productSlice = productRepository.findByLastUpdatedBeforeOrLastUpdatedIsNull(threshold, pageable);

            log.info("Processing page {}, items: {}", pageNumber, productSlice.getNumberOfElements());

            for (Product product : productSlice.getContent()) {
                sendPriceCheckEvent(product);
            }

            pageNumber++;
        } while (productSlice.hasNext());

        log.info("Total pages processed: {}", pageNumber);
    }

    private void sendPriceCheckEvent(Product product) {
        PriceCheckEvent event = new PriceCheckEvent(product.getId(), product.getUrl());

        kafkaTemplate.send(priceCheckTopic, product.getId().toString(), event).whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("Failed to schedule price check for product {}: {}", product.getId(), ex.getMessage());
            }
        });
    }
}
