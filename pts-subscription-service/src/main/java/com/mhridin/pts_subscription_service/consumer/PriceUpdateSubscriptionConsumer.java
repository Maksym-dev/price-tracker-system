package com.mhridin.pts_subscription_service.consumer;

import com.mhridin.pts_common.entity.Product;
import com.mhridin.pts_common.entity.Subscription;
import com.mhridin.pts_common.exception.ProductNotFoundException;
import com.mhridin.pts_common.kafka.PriceUpdateEvent;
import com.mhridin.pts_common.repository.ProductRepository;
import com.mhridin.pts_common.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PriceUpdateSubscriptionConsumer {

    private final ProductRepository productRepository;
    private final SubscriptionRepository subscriptionRepository;

    @KafkaListener(topics = "price-update-events", groupId = "subscription-group")
    public void consumePriceUpdate(PriceUpdateEvent event) {
        if (!"SUCCESS".equals(event.getStatus())) {
            log.warn("Price update failed for product id: {}", event.getProductId());
            return;
        }

        Product product = productRepository.findById(event.getProductId())
                .orElseThrow(() -> new ProductNotFoundException("Product with id " + event.getProductId() + " not found"));

        BigDecimal oldPrice = product.getCurrentPrice();
        product.setCurrentPrice(event.getNewPrice());
        product.setIsAvailable(event.getAvailableStatus());
        product.setLastUpdated(LocalDateTime.now());
        log.info("Price update for product id: {}, old price: {}, new price: {}",
                event.getProductId(), oldPrice, event.getNewPrice());
        productRepository.save(product);

        List<Subscription> activeSubscriptions = subscriptionRepository
                .findAllByProductIdAndIsActiveTrue(product.getId());

        for (Subscription sub : activeSubscriptions) {
            if (shouldNotify(sub, event.getNewPrice(), event.getAvailableStatus())) {
                sendNotificationEvent(sub, product, event.getNewPrice());
            }
        }
    }

    private boolean shouldNotify(Subscription sub, BigDecimal newPrice, Boolean availableStatus) {
        return Boolean.TRUE.equals(availableStatus) && newPrice.compareTo(sub.getTargetPrice()) <= 0;
    }

    private void sendNotificationEvent(Subscription sub, Product product, BigDecimal newPrice) {
        log.info("TODO: Send message to Kafka for Notification Service");
    }
}
