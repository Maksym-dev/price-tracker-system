package com.mhridin.pts_subscription_service.controller;

import com.mhridin.pts_common.dto.SubscriptionDto;
import com.mhridin.pts_common.entity.Product;
import com.mhridin.pts_common.entity.Subscription;
import com.mhridin.pts_common.entity.User;
import com.mhridin.pts_common.exception.ProductNotFoundException;
import com.mhridin.pts_common.exception.SubscriptionNotFoundException;
import com.mhridin.pts_common.exception.UserNotFoundException;
import com.mhridin.pts_common.kafka.PriceCheckEvent;
import com.mhridin.pts_common.repository.ProductRepository;
import com.mhridin.pts_common.repository.SubscriptionRepository;
import com.mhridin.pts_common.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/subscriptions")
@Slf4j
public class SubscriptionRestController {

    private final UserRepository userRepository;

    private final ProductRepository productRepository;

    private final SubscriptionRepository subscriptionRepository;

    private final KafkaTemplate<String, PriceCheckEvent> kafkaTemplate;

    @Value("${app.kafka.topics.price-check}")
    private String priceCheckTopic;

    public SubscriptionRestController(UserRepository userRepository, ProductRepository productRepository, SubscriptionRepository subscriptionRepository, KafkaTemplate<String, PriceCheckEvent> kafkaTemplate) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @GetMapping
    public ResponseEntity<Page<SubscriptionDto>> getAllSubscriptions(@PageableDefault(sort = "targetPrice", direction = Sort.Direction.ASC)
                                                                         Pageable pageable) {
        Page<Subscription> all = subscriptionRepository.findAll(pageable);
        Page<SubscriptionDto> dtoPage = all.map(SubscriptionDto::new);
        return ResponseEntity.ok(dtoPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionDto> getSubscriptionById(@PathVariable Long id) {
        Subscription subscription = subscriptionRepository.findById(id).orElseThrow(() -> new SubscriptionNotFoundException("Subscription with id " + id + " not found"));
        SubscriptionDto dto = new SubscriptionDto();
        dto.setId(subscription.getId());
        dto.setProductId(subscription.getProduct().getId());
        dto.setUserId(subscription.getUser().getId());
        dto.setTargetPrice(subscription.getTargetPrice());
        dto.setActive(subscription.isActive());
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<Subscription> createSubscription(@RequestBody SubscriptionDto subscriptionDto) {
        User user = userRepository.findById(subscriptionDto.getUserId()).orElseThrow(() -> new UserNotFoundException("User with id " + subscriptionDto.getUserId() + " not found"));
        Product product = productRepository.findById(subscriptionDto.getProductId()).orElseThrow(() -> new ProductNotFoundException("Product with id " + subscriptionDto.getProductId() + " not found"));

        Subscription subscription = new Subscription();
        subscription.setUser(user);
        subscription.setProduct(product);
        subscription.setTargetPrice(subscriptionDto.getTargetPrice());
        subscription.setActive(subscriptionDto.isActive());

        Subscription savedSubscription = subscriptionRepository.save(subscription);
        sendPriceCheckEvent(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedSubscription);
    }

    private void sendPriceCheckEvent(Product product) {
        PriceCheckEvent event = new PriceCheckEvent(product.getId(), product.getUrl());

        kafkaTemplate.send(priceCheckTopic, product.getId().toString(), event)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        log.info("Sent price check task for product id: {}", product.getId());
                    } else {
                        log.error("Failed to send price check task", ex);
                    }
                });
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateSubscription(@PathVariable("id") Long id, @RequestBody Subscription subscription) {
        if (!Objects.equals(subscription.getId(), id)) {
            throw new IllegalStateException("Subscription id and path variable are not the same");
        }
        Subscription fromDB = subscriptionRepository.findById(id).orElse(null);
        if (fromDB == null) {
            throw new SubscriptionNotFoundException("Subscription with id " + id + " not found");
        }
        fromDB.setActive(subscription.isActive());
        fromDB.setTargetPrice(subscription.getTargetPrice());
        subscriptionRepository.save(fromDB);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubscription(@PathVariable("id") Long id) {
        Subscription fromDB = subscriptionRepository.findById(id).orElse(null);
        if (fromDB == null) {
            throw new SubscriptionNotFoundException("Subscription with id " + id + " not found");
        }
        subscriptionRepository.delete(fromDB);
        return ResponseEntity.noContent().build();
    }
}
